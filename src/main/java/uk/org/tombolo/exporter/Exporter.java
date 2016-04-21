package uk.org.tombolo.exporter;

import java.io.Writer;

import uk.org.tombolo.execution.spec.DatasetSpecification;

public interface Exporter {

	public void write(Writer writer, DatasetSpecification datasetSpecification) throws Exception;
}