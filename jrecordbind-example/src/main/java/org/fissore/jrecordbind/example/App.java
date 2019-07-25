package org.fissore.jrecordbind.example;

import org.fissore.jrecordbind.*;
import org.fissore.jrecordbind.padders.AbstractRightPadder;
import org.jrecordbind.schemas.jrb.example.ExampleRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

  public static void main(String[] args) {
    DateConverter dateConverter = new DateConverter("yyyyMMdd");
    AbstractRightPadder underscorePadder = new AbstractRightPadder('_') {

    };

    Map<String, Converter> converters = Map.of("date_converter", dateConverter);
    Map<String, Padder> padders = Map.of("underscore_padder", underscorePadder);

    RecordDefinition recordDefinition = new DefinitionLoader().load(Path.of(App.class.getResource("/simple.def.xsd").getFile()).toFile());

    Unmarshaller<ExampleRecord> unmarshaller = new Unmarshaller<>(recordDefinition, new SimpleLineReader(), converters, padders);

    List<ExampleRecord> records = unmarshaller.unmarshallToStream(new InputStreamReader(App.class.getResourceAsStream("/simple.txt"))).collect(Collectors.toList());

    assert records.size() == 1;

    ExampleRecord record = records.get(0);
    System.out.println("Unmarshaller result:");
    System.out.printf("Name: '%s', Surname: '%s', Tax code: '%s', Birthday: %tD\n",
      record.getName(), record.getSurname(), record.getTaxCode(), record.getBirthday());
    System.out.printf("OneInteger: '%d', TwoInteger: '%d', OneFloat: '%f', Selected: %b\n",
      record.getOneInteger(), record.getTwoInteger(), record.getOneFloat(), record.isSelected());

    System.out.println();

    ExampleRecord aNewRecord = new ExampleRecord();
    aNewRecord.setName("Walter");
    aNewRecord.setSurname("Lippman");
    aNewRecord.setBirthday(Calendar.getInstance());
    aNewRecord.setSelected(false);

    Marshaller<ExampleRecord> marshaller = new Marshaller<>(recordDefinition, converters, padders);
    StringWriter writer = new StringWriter();
    marshaller.marshall(aNewRecord, writer);

    System.out.println("Marshaller result:");
    System.out.println(writer.toString());
  }

}
