/*
 * Copyright (c) 2025 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.jsoup;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Utility class for converting W3C DOM Node trees to jsoup Node trees.
 *
 * @author Ronald Brill
 */
public class HtmlUnitDOMToJsoupConverter {

    /**
     * Converts a W3C DOM Node to a jsoup Node.
     *
     * @param w3cNode The W3C DOM node to convert
     * @return The equivalent jsoup node, or null if conversion fails
     */
    public static Node convert(final org.w3c.dom.Node w3cNode) {
        if (w3cNode == null) {
            return null;
        }

        switch (w3cNode.getNodeType()) {
            case org.w3c.dom.Node.ELEMENT_NODE:
                return convertElement(w3cNode);

            case org.w3c.dom.Node.TEXT_NODE:
                return convertTextNode(w3cNode);

            case org.w3c.dom.Node.COMMENT_NODE:
                return convertCommentNode(w3cNode);

            case org.w3c.dom.Node.DOCUMENT_NODE:
                return convertDocumentNode(w3cNode);

            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                return convertCDataNode(w3cNode);

            default:
                // For unsupported node types, return null or handle as needed
                return null;
        }
    }

    /**
     * Converts a W3C DOM Element to a jsoup Element
     */
    private static Element convertElement(final org.w3c.dom.Node w3cNode) {
        final String tagName = w3cNode.getNodeName().toLowerCase();
        final Element jsoupElement = new Element(Tag.valueOf(tagName), "");

        // Convert attributes
        final NamedNodeMap attributes = w3cNode.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                final org.w3c.dom.Node attr = attributes.item(i);
                jsoupElement.attr(attr.getNodeName(), attr.getNodeValue());
            }
        }

        // Convert child nodes recursively
        final NodeList children = w3cNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node jsoupChild = convert(children.item(i));
            if (jsoupChild != null) {
                jsoupElement.appendChild(jsoupChild);
            }
        }

        return jsoupElement;
    }

    /**
     * Converts a W3C DOM Text Node to a jsoup TextNode
     */
    private static TextNode convertTextNode(final org.w3c.dom.Node w3cNode) {
        final String text = w3cNode.getNodeValue();
        return new TextNode(text != null ? text : "");
    }

    /**
     * Converts a W3C DOM Comment Node to a jsoup Comment
     */
    private static Comment convertCommentNode(final org.w3c.dom.Node w3cNode) {
        final String commentText = w3cNode.getNodeValue();
        return new Comment(commentText != null ? commentText : "");
    }

    /**
     * Converts a W3C DOM Document Node to a jsoup Document
     */
    private static Document convertDocumentNode(final org.w3c.dom.Node w3cNode) {
        final Document jsoupDoc = new Document("");

        // Convert child nodes (typically the root element)
        final NodeList children = w3cNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node jsoupChild = convert(children.item(i));
            if (jsoupChild != null) {
                jsoupDoc.appendChild(jsoupChild);
            }
        }

        return jsoupDoc;
    }

    /**
     * Converts a W3C DOM CDATA Section to a jsoup DataNode
     */
    private static DataNode convertCDataNode(final org.w3c.dom.Node w3cNode) {
        final String data = w3cNode.getNodeValue();
        return new DataNode(data != null ? data : "");
    }

    /**
     * Convenience method to convert an entire W3C Document to a jsoup Document.
     *
     * @param w3cDocument The W3C DOM Document to convert
     * @return The equivalent jsoup Document
     */
    public static Document convertDocument(final org.w3c.dom.Document w3cDocument) {
        if (w3cDocument == null) {
            return null;
        }

        final Node converted = convert(w3cDocument);
        return (converted instanceof Document) ? (Document) converted : null;
    }

    /**
     * Convenience method to convert a W3C Element to a jsoup Element.
     *
     * @param w3cElement The W3C DOM Element to convert
     * @return The equivalent jsoup Element
     */
    public static Element convertElement(final org.w3c.dom.Element w3cElement) {
        if (w3cElement == null) {
            return null;
        }

        final Node converted = convert(w3cElement);
        return (converted instanceof Element) ? (Element) converted : null;
    }
}
