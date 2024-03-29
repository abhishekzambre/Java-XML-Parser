package com.company;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.*;

public class Main {

    public static void main(String[] args) {

        parseNessus(args[0]);

    }


    public static void parseNessus(String nessusReport) {

        try {

            SAXReader saxReader = new SAXReader();

            FileWriter fr = new FileWriter("output.txt");

            Document document = saxReader.read(nessusReport);

            // each entry is indexed by one cve_id
            List reportHost = document.selectNodes("/*[local-name(.)='NessusClientData_v2']/*[local-name(.)='Report']/*[local-name(.)='ReportHost']");
            Iterator reportHostItrt = reportHost.iterator();

            while (reportHostItrt.hasNext()) {

                Element host = (Element) reportHostItrt.next();

                //	System.out.println("host name is: "+host.attribute(0).getText());


                // element iterator of each entry
                Iterator ei = host.elementIterator();

                // put all of the subelements' names(subelement of entry) to
                // an array list(subele)
                while (ei.hasNext()) {

                    Element sube = (Element) ei.next();
                    //	System.out.println("attribute count is: "+sube.attributeCount());
                    if(!sube.getName().equals("ReportItem"))
                        continue;

                    // a list of elements for each entry
                    ArrayList<String> subele = new ArrayList<String>();

                    Iterator reportItemItrt = sube.elementIterator();
                    while(reportItemItrt.hasNext()){

                        Element reportItemElement = (Element) reportItemItrt.next();
                        //		System.out.println(reportItemElement.getName());
                        subele.add(reportItemElement.getName());
                    }

                    if(subele.size()==0||(!subele.contains("cve")))
                        continue;

                    Iterator itr = sube.elementIterator("cve");
                    while(itr.hasNext()){

                        System.out.println("host name is: "+host.attribute(0).getText());

                        fr.write(host.attribute(0).getText()+ "\n");

                        Element cve = (Element) itr.next();

                        System.out.println(cve.getText());

                        fr.write(cve.getText()+ "\n");

                        System.out.println("port number is: "+sube.attribute(0).getText());

                        fr.write(sube.attribute(0).getText()+ "\n");

                        System.out.println("protocol is: "+sube.attribute(2).getText());

                        fr.write(sube.attribute(2).getText()+ "\n");

                        System.out.println();

                        //	fr.write("\n");

                    }



                }
            } // end of each entry's processing

            fr.close();

            // print out the stack trace for each exception(either documentation
            // exception or IO exception).
        }
        catch (DocumentException e) {

            e.printStackTrace();

        }
        catch (IOException e) {

            e.printStackTrace();

        }

    }// end of collect().
}
