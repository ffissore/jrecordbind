package it.assist.jarb.gen;

import it.assist.jarb.DefinitionLoader;
import it.assist.jarb.RecordDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.tools.ant.Project;

public class GeneratorTask {

  private String destPath;
  private Project project;
  private String specFile;

  public void execute() throws Exception {
    if (specFile == null) {
      throw new IllegalStateException("'specFile' argument missing");
    }
    if (destPath == null) {
      throw new IllegalStateException("'destPath' argument missing");
    }

    DefinitionLoader definitionLoader = new DefinitionLoader();
    String specFileReview = specFile;
    if (!specFileReview.startsWith("/")) {
      specFileReview = "/" + specFileReview;
    }
    definitionLoader.load(getClass().getResourceAsStream(specFileReview));

    RecordDefinition definition = definitionLoader.getDefinition();
    String completeFolderName = project.getBaseDir().getAbsolutePath() + File.separator + destPath + File.separator
        + definition.getPackageName().replaceAll("\\.", File.separator) + File.separator;
    new File(completeFolderName).mkdirs();

    FileOutputStream fos = new FileOutputStream(completeFolderName + definition.getClassName() + ".java");
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    new BeanGenerator().generate(definition, osw);

    osw.close();
    fos.close();
  }

  public String getDestPath() {
    return destPath;
  }

  public String getSpecFile() {
    return specFile;
  }

  public void setDestPath(String destPath) {
    this.destPath = destPath;
  }

  public void setProject(Project proj) {
    project = proj;
  }

  public void setSpecFile(String specFile) {
    this.specFile = specFile;
  }

}
