package com.highspot.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highspot.playlist.processor.SimpleActionProcessor;
import picocli.CommandLine;

/**
 * entry point of the application.
 * IngestApp is a application which helps ingest a mixtage.json with a change file.
 */
public class IngestApp {
  public static void main(String[] args) {
    final Ingester ingester = new Ingester(new ObjectMapper(),
                                           new SimpleActionProcessor());
    final CommandLine commandLine = new CommandLine(ingester);
    System.exit(commandLine.execute(args));
  }
}
