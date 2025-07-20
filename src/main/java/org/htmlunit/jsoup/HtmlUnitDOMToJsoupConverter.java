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

import java.util.Locale;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Tag;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Utility class for converting W3C DOM Node trees to jsoup Node trees.
 * This class uses the Builder pattern.
 *
 * @author Ronald Brill
 */
public final class HtmlUnitDOMToJsoupConverter {

    public static class Builder {

        protected Builder() {
            super();
        }

        /**
         * @return the configured {@link HtmlUnitDOMToJsoupConverter}
         */
        public HtmlUnitDOMToJsoupConverter build() {
            return new HtmlUnitDOMToJsoupConverter();
        }
    }

    /**
     * Create a new builder for configuring an {@link HtmlUnitDOMToJsoupConverter}.
     *
     * @return a {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private ctor - use the builder to create a new converter.
     */
    HtmlUnitDOMToJsoupConverter() {
        super();
    }

    /**
     * Converts a W3C DOM Node to a jsoup Node.
     * This converts only the node itself and all children. In some cases it might
     * be more helpfull to use {@link #convertWholeTree(org.w3c.dom.Node)}.
     *
     * @param w3cNode The W3C DOM node to convert
     * @return The equivalent jsoup node, or null if conversion fails
     */
    public Node convert(final org.w3c.dom.Node w3cNode) {
        return convert(w3cNode, new Result(null));
    }

    /**
     * Converts a W3C DOM Node to a jsoup Node.
     * This starts the conversion at the root of the node tree. For most
     * cases this is the better approach because the resulting node is
     * traversable in all directions.
     *
     * @param w3cNode The W3C DOM node to convert
     * @return The equivalent jsoup node, or null if conversion fails
     */
    public Node convertWholeTree(final org.w3c.dom.Node w3cNode) {
        if (w3cNode == null) {
            return null;
        }

        // find root
        org.w3c.dom.Node w3cRootNode = w3cNode;
        org.w3c.dom.Node w3cParentNode = w3cRootNode.getParentNode();
        while (w3cParentNode != null) {
            w3cRootNode = w3cParentNode;
            w3cParentNode = w3cRootNode.getParentNode();
        }

        final Result result = new Result(w3cNode);
        convert(w3cRootNode, result);
        return result.getResult();
    }

    private Node convert(final org.w3c.dom.Node w3cNode, final Result result) {
        if (w3cNode == null) {
            return null;
        }

        switch (w3cNode.getNodeType()) {
            case org.w3c.dom.Node.ELEMENT_NODE:
                final String tagName = w3cNode.getNodeName().toLowerCase(Locale.ROOT);
                final Element jsoupElement = new Element(Tag.valueOf(tagName), null);
                result.track(w3cNode, jsoupElement);
                return convertNode(w3cNode, jsoupElement, result);

            case org.w3c.dom.Node.TEXT_NODE:
                final String text = w3cNode.getNodeValue();
                final TextNode textNode = new TextNode(text != null ? text : "");
                result.track(w3cNode, textNode);
                return textNode;

            case org.w3c.dom.Node.COMMENT_NODE:
                final String commentText = w3cNode.getNodeValue();
                final Comment comment = new Comment(commentText != null ? commentText : "");
                result.track(w3cNode, comment);
                return comment;

            case org.w3c.dom.Node.DOCUMENT_NODE:
                final Document jsoupDoc = new Document("");
                result.track(w3cNode, jsoupDoc);
                return convertNode(w3cNode, jsoupDoc, result);

            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                final String data = w3cNode.getNodeValue();
                final DataNode dataNode = new DataNode(data != null ? data : "");
                result.track(w3cNode, dataNode);
                return dataNode;

            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                final XmlDeclaration xmlDeclaration = convertProcessingInstruction(w3cNode);
                result.track(w3cNode, xmlDeclaration);
                return xmlDeclaration;

            case org.w3c.dom.Node.DOCUMENT_TYPE_NODE:
                final DocumentType documentType = convertDocumentType(w3cNode);
                result.track(w3cNode, documentType);
                return documentType;

            default:
                // For unsupported node types, return null
                return null;
        }
    }

    private Element convertNode(final org.w3c.dom.Node w3cNode, final Element jsoupElement, final Result result) {
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
            final Node jsoupChild = convert(children.item(i), result);
            if (jsoupChild != null) {
                jsoupElement.appendChild(jsoupChild);
            }
        }

        return jsoupElement;
    }

    /**
     * Convenience method to convert an entire W3C Document to a jsoup Document.
     *
     * @param w3cDocument The W3C DOM Document to convert
     * @return The equivalent jsoup Document
     */
    public Document convertDocument(final org.w3c.dom.Document w3cDocument) {
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
    public Element convertElement(final org.w3c.dom.Element w3cElement) {
        if (w3cElement == null) {
            return null;
        }

        final Node converted = convert(w3cElement);
        return (converted instanceof Element) ? (Element) converted : null;
    }

    /**
     * Converts a W3C DOM Processing Instruction to a JSoup XmlDeclaration
     */
    private static XmlDeclaration convertProcessingInstruction(final org.w3c.dom.Node w3cNode) {
        final String target = w3cNode.getNodeName();
        final String data = w3cNode.getNodeValue();

        if ("xml".equals(target)) {
            return new XmlDeclaration("xml", false);
        }

        // For other processing instructions, create a comment as fallback
        return new XmlDeclaration(target + " " + (data != null ? data : ""), true);
    }

    /**
     * Converts a W3C DOM Document Type to a JSoup DocumentType
     */
    private DocumentType convertDocumentType(final org.w3c.dom.Node w3cNode) {
        final org.w3c.dom.DocumentType docType = (org.w3c.dom.DocumentType) w3cNode;

        final String name = docType.getName();
        final String publicId = docType.getPublicId();
        final String systemId = docType.getSystemId();

        return new DocumentType(
            name != null ? name : "",
            publicId != null ? publicId : "",
            systemId != null ? systemId : ""
        );
    }

    private static class Result {
        private final org.w3c.dom.Node w3cNode_;
        private Node result_;

        Result(final org.w3c.dom.Node w3cNode) {
            w3cNode_ = w3cNode;
        }

        public Node getResult() {
            return result_;
        }

        void track(final org.w3c.dom.Node w3cNode, final Node jsoupNode) {
            if (w3cNode_ != null && w3cNode_ == w3cNode) {
                result_ = jsoupNode;
            }
        }
    }
}
