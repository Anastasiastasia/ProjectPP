import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
public class readwrite {
private static StringBuilder result = new StringBuilder();
protected static void readTxt(String filePath,boolean encrypt,boolean zip) {
   if (zip) {
        inZipMeals(filePath);
        filePath = "new_" + filePath;
        }
    try {
         String content = new String(Files.readAllBytes(Paths.get(filePath)));
         String[]lines;
    if(encrypt)
     { String decryptedContent = decrypt(content);
      lines = decryptedContent.split("\n");}
     else
         {lines = content.split("\n");}
     for (String line : lines) {
            String processedLine = arithmetic.processLine(line);
            result.append(processedLine).append("\n");
         }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
}
protected static void writeTxt(String filePath,boolean encrypt,boolean zip) {
        try {
            if(encrypt)
           { String encryptedContent = encrypt(result.toString());
            Files.write(Paths.get(filePath), encryptedContent.getBytes());}
           else{ Files.write(Paths.get(filePath), result.toString().getBytes());}
            if (zip) {
                toZipMeals(filePath);}
            System.out.println("Output written to " + filePath);
             } catch (IOException e) {
              System.err.println("Error: " + e.getMessage());
             }  }
 protected static void readXml(String filePath,boolean encrypt,boolean zip ) {
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document;
        if (zip) {
        inZipMeals(filePath);
        filePath = "new_" + filePath;  }
         if(encrypt) {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        String decryptedContent = decrypt(content);
        document = builder.parse(new ByteArrayInputStream(decryptedContent.getBytes()));
         }
        else{ 
        document = builder.parse(new File(filePath));
         }
        NodeList lineNodes = document.getElementsByTagName("line");
        for (int i = 0; i < lineNodes.getLength(); i++) {
        Element lineElement = (Element) lineNodes.item(i);
        String originalText = lineElement.getTextContent();
        String processedText = arithmetic.processLine(originalText);
        result.append(processedText).append("\n"); }
        } catch (Exception e) {
         e.printStackTrace();
         } }
 protected static void writeXml(String filePath,boolean encrypt,boolean zip) {
     try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootElement = document.createElement("lines");
        document.appendChild(rootElement);
        String[] lines = result.toString().split("\n");
          for (String line : lines) {
         Element lineElement = document.createElement("line");
         lineElement.setTextContent(line);
         rootElement.appendChild(lineElement);
         }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult resultOutput = new StreamResult(new File(filePath));
        if(encrypt)
         { String encryptedContent = encrypt(documentToString(document));
           Files.write(Paths.get(filePath), encryptedContent.getBytes());}
        else{
            transformer.transform(source, resultOutput);
            }
        if (zip) { toZipMeals(filePath); }
         System.out.println("Output written to " + filePath);
         } catch (Exception e) {
            e.printStackTrace();
         }
 }
private static String documentToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
}
protected static void TypeofReadWrite(String s,String w,boolean encrypt,boolean zip)
{  if (s.equals("txt")) 
      {
        if(w.equals("read"))
            {readTxt("input.txt",encrypt,zip);}
        if(w.equals("write"))
            {writeTxt("output.txt",encrypt,zip);}
  } else if (s.equals("xml")) {
        if(w.equals("read"))
            {readXml("input.xml",encrypt,zip);}
        if(w.equals("write"))
            {writeXml("output.xml",encrypt,zip);}
} else if (s.equals("json")) {
        if(w.equals("read"))
             {readJson("input.json",encrypt,zip);}
        if(w.equals("write"))
              {writeJson("output.json",encrypt,zip);}
   } else {
         System.out.println("Неверный формат.");
   } }
private static final String key = "ThisIsASecretKey"; 
 public static String encrypt(String value) {
         try {
         Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
         e.printStackTrace();
        }
         return null;
        }
 public static String decrypt(String encryptedValue) {
         try {
         Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.DECRYPT_MODE, secretKey);
         byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
         return new String(decryptedValue);
         } catch (Exception e) {
         e.printStackTrace();
          }
         return null;
        }
public static void toZipMeals(String filename) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("Output.zip"));
            FileInputStream fis = new FileInputStream(filename);) {
            ZipEntry entry1 = new ZipEntry(filename);
            zout.putNextEntry(entry1);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zout.write(buffer);
            zout.closeEntry();
        } 
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
public static void inZipMeals(String filePath) {
        String zipFilePath = "Input.zip"; 
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                System.out.printf("Файл в ZIP-архиве: %s \n", name);
                try (FileOutputStream fout = new FileOutputStream("new_" + name)) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    fout.flush();
                    System.out.printf("Данные из ZIP-архива извлекаем в новый файл: %s \n", "new_" + name);
                }
                zin.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
protected static void readJson(String filePath, boolean encrypt, boolean zip) {
        if (zip) {
            inZipMeals(filePath);
            filePath = "new_" + filePath; 
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            if (encrypt) {
                content = decrypt(content);
            }
            content = content.trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1); 
            }
            String[] lines = content.split(",\\s*"); 
            for (String line : lines) {
                line = line.trim().replace("\"", ""); 
                if (!line.isEmpty()) { 
                    String processedText = arithmetic.processLine(line);
                    result.append(processedText).append("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }
protected static void writeJson(String filePath, boolean encrypt, boolean zip) {
        try {
            StringBuilder jsonContent = new StringBuilder("[\n");
            String[] lines = result.toString().split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) { 
                    jsonContent.append("  \"").append(line.replace("\"", "\\\"")).append("\",\n"); 
                }
            }
            if (jsonContent.length() > 2) {
                jsonContent.setLength(jsonContent.length() - 2); 
            }
            jsonContent.append("\n]");
            String finalContent = jsonContent.toString();
            if (encrypt) {
                finalContent = encrypt(finalContent);
            }
            Files.write(Paths.get(filePath), finalContent.getBytes());
            if (zip) {
                toZipMeals(filePath);
            }
            System.out.println("Output written to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

       
      
  

