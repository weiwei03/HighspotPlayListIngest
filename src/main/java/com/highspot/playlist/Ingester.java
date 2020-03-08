package com.highspot.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highspot.playlist.action.Action;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.processor.ActionProcessor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * the executor of file ingest, it ingests mixtape.json with change file,
 * and stores the data to the output file.
 */
@CommandLine.Command(
  name = "ingest",
  mixinStandardHelpOptions = true,
  version = "1.0",
  description = "ingest is a commandline tool which is used to"
                  + " ingest a mixtage.json with a change file",
  subcommands = CommandLine.HelpCommand.class
)
@Slf4j
public class Ingester implements Callable<Integer> {
  @CommandLine.Option(names = {"-i", "--input"},
    description = "Input file path for mixtape.json")
  @Setter
  private File inputFile;

  @CommandLine.Option(names = {"-c", "--change"},
    description = "File path for the change file ")
  @Setter
  private File changeFile;

  @CommandLine.Option(names = {"-o", "--output"},
    description = "Output file path")
  @Setter
  private File outputFile;

  private final ObjectMapper objectMapper;
  private final ActionProcessor actionProcessor;

  public Ingester(final ObjectMapper objectMapper, final ActionProcessor actionProcessor) {
    this.objectMapper = objectMapper;
    this.actionProcessor = actionProcessor;
  }

  /**
   * read the data from mixtape.json and change file,
   * execute file ingestion and write data to the output file.
   * @return integer
   *  0 : success
   *  1 : error
   */
  @Override
  public Integer call() {
    try {
      final MixTape mixTape = loadMixTapeFile(inputFile);
      final Action[] actionList = loadActionFile(changeFile);

      actionProcessor.process(mixTape, actionList);
      saveResult(outputFile, mixTape);
      log.info(String.format("succeeded ingesting %s with %s",
                             inputFile.getName(), changeFile.getName()));
      return 0;
    } catch (Exception e) {
      log.error(String.format("Failed to ingest %s with %s, reason: %s",
                              inputFile.getName(), changeFile.getName(), e.getMessage()));
      return 1;
    }
  }

  private MixTape loadMixTapeFile(final File input) throws Exception {
    try {
      return objectMapper.readValue(input, MixTape.class);
    } catch (Exception e) {
      final String errorMessage = String.format("load mixtape file failed, error:%s",
                                                e.getMessage());
      throw new Exception(errorMessage, e);
    }
  }

  private Action[] loadActionFile(final File actions) throws Exception {
    try {
      return objectMapper.readValue(actions, Action[].class);
    } catch (Exception e) {
      final String errorMessage = String.format("load changes file failed, error:%s",
                                                e.getMessage());
      throw new Exception(errorMessage, e);
    }
  }

  private void saveResult(final File output, final MixTape mixTape) throws Exception {
    try {
      objectMapper.writerWithDefaultPrettyPrinter()
                  .writeValue(output, mixTape);
    } catch (Exception e) {
      final String errorMessage = String.format("write output file failed, error:%s",
                                                e.getMessage());
      throw new Exception(errorMessage, e);
    }
  }
}
