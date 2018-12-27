package com.hz.maiku.maikumodule.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.XmlResourceParser;

import com.hz.maiku.maikumodule.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/27
 * @email 252774645@qq.com
 */
public class XmlFileUtil {

    private static List<String> commonNodeArr = new ArrayList<String>();


    public static void updateXML(Context context,String key,String value,int id){
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File file = new File("E:/progect/tools/Android-Tools/PhoneManager/maikumodule/src/main/res/values/colors.xml");
            Document doc = newDocumentBuilder.parse(file);
            Element root = doc.getDocumentElement();
            NodeList personList = root.getElementsByTagName("color");
            Node item = personList.item(id);
            Element personElement = (Element) item;
            NodeList nameList = personElement.getElementsByTagName(key);
            nameList.item(0).setTextContent(value);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(doc);
            Result result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据节点参数查询节点
     * @param express 节点路径
     * @param source 搜索节点源
     * @return 查询到的第一个节点
     */
    public static Node selectSingleNode(String express, Element source) {
        Node result = null;
        //创建XPath工厂
        XPathFactory xPathFactory = XPathFactory.newInstance();
        //创建XPath对象
        XPath xpath = xPathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source,XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
            return null;
        }
        return result;
    }


    @SuppressLint("ResourceType")
    public XmlResourceParser getXMLFromResXml(Context context, String fileName){
        XmlResourceParser xmlParser = null;
        try {
            //*/
            //  xmlParser = this.getResources().getAssets().openXmlResourceParser("assets/"+fileName);        // 失败,找不到文件
            xmlParser = context.getResources().getXml(R.color.colorPrimary);
            /*/
            // xml文件在res目录下 也可以用此方法返回inputStream
            InputStream inputStream = this.getResources().openRawResource(R.xml.provinceandcity);
            /*/
            return xmlParser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlParser;
    }



}
