package com.bytecode.springml;

import com.bytecode.springml.repository.SeguroRepositorio;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.*;

import java.io.File;

@SpringBootApplication
public class App implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	private static final Log logger = LogFactory.getLog(App.class);

	@Autowired
	private SeguroRepositorio seguroRepositorio;

	@Override
	public void run(String... args) throws Exception {

	}

	@Bean
	public LinearRegression linearRegression() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = objectMapper.readTree(
				objectMapper.writeValueAsBytes(seguroRepositorio.findDescribe(Pageable.unpaged()))
		);

		CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
		JsonNode firstObject = jsonNode.elements().next();
		firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
		CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

		CsvMapper csvMapper = new CsvMapper();
		csvMapper.writerFor(JsonNode.class)
				.with(csvSchema)
				.writeValue(new File("src/main/resources/data.csv"), jsonNode);

		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("src/main/resources/data.csv"));
		Instances data = loader.getDataSet();

		File fileArrf = new File("src/main/resources/seguro.arff");

		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(fileArrf);
		saver.writeBatch();

		ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileArrf.getAbsolutePath());
		Instances instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);

		double percent = 70;

		int trainSize = (int) Math.round(instances.numInstances() * percent
				/ 100);
		int testSize = instances.numInstances() - trainSize;

		Instances train = new Instances(instances, 0, trainSize);
		Instances test = new Instances(instances, trainSize, testSize);

		logger.info(instances.numInstances() + " instancias");
		logger.info(instances.numAttributes() + " atributos");

		LinearRegression linearRegression = new LinearRegression();
		linearRegression.buildClassifier(train);

		Evaluation linearRegressionEvaluation = new Evaluation(test);
		linearRegressionEvaluation.evaluateModel(linearRegression, test);
		System.out.println(linearRegressionEvaluation.toSummaryString());
		return linearRegression;
	}
}
