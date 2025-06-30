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
package org.htmlunit.jsoup.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.opentest4j.AssertionFailedError;

/**
 * JUnit 5 assertion utility for deep comparison of JSoup nodes.
 *
 * @author Ronald Brill
 */
public final class JSoupAssertions {

    private JSoupAssertions() {
        // Utility class - prevent instantiation
    }

    /**
     * Asserts that two JSoup nodes are deeply equal.
     */
    public static void assertNodesEqual(final Node expected, final Node actual) {
        assertNodesEqual(expected, actual, (String) null);
    }

    /**
     * Asserts that two JSoup nodes are deeply equal with custom message.
     */
    public static void assertNodesEqual(final Node expected, final Node actual, final String message) {
        assertNodesEqual(expected, actual, () -> message);
    }

    /**
     * Asserts that two JSoup nodes are deeply equal with custom message supplier.
     */
    public static void assertNodesEqual(final Node expected, final Node actual, final Supplier<String> messageSupplier) {
        final List<String> differences = new ArrayList<>();
        final boolean isEqual = compareNodes(expected, actual, "", differences);

        if (!isEqual) {
            final String prefix;
            if (messageSupplier != null && messageSupplier.get() != null) {
                prefix = messageSupplier.get() + " ==> ";
            }
            else {
                prefix = "";
            }
            final String failureMessage = prefix + "JSoup nodes are not equal:\n" + String.join("\n", differences);
            throw new AssertionFailedError(failureMessage, expected, actual);
        }
    }

    private static boolean compareNodes(final Node node1, final Node node2, final String path, final List<String> differences) {
        if (node1 == null && node2 == null) {
            return true;
        }

        if (node1 == null) {
            differences.add(path + ": Expected node is null, but actual is " + node2.getClass().getSimpleName());
            return false;
        }

        if (node2 == null) {
            differences.add(path + ": Actual node is null, but expected is " + node1.getClass().getSimpleName());
            return false;
        }

        // Check node types
        if (!node1.getClass().equals(node2.getClass())) {
            differences.add(path
                                + ": Different node types - expected "
                                + node1.getClass().getSimpleName()
                                + " but was "
                                + node2.getClass().getSimpleName());
            return false;
        }

        boolean isEqual = true;

        // Compare based on node type
        if (node1 instanceof Element) {
            isEqual &= compareElements((Element) node1, (Element) node2, path, differences);
        }
        else if (node1 instanceof TextNode) {
            isEqual &= compareTextNodes((TextNode) node1, (TextNode) node2, path, differences);
        }
        else if (node1 instanceof Comment) {
            isEqual &= compareComments((Comment) node1, (Comment) node2, path, differences);
        }
        else if (node1 instanceof DataNode) {
            isEqual &= compareDataNodes((DataNode) node1, (DataNode) node2, path, differences);
        }
        else if (node1 instanceof DocumentType) {
            isEqual &= compareDocumentTypes((DocumentType) node1, (DocumentType) node2, path, differences);
        }

        // Compare children
        isEqual &= compareChildren(node1, node2, path, differences);

        return isEqual;
    }

    private static boolean compareElements(final Element elem1, final Element elem2, final String path, final List<String> differences) {
        boolean isEqual = true;

        // Compare tag names
        if (!elem1.tagName().equals(elem2.tagName())) {
            differences.add(path + ": Different tag names - expected '" + elem1.tagName() + "' but was '" + elem2.tagName() + "'");
            isEqual = false;
        }

        // Compare attributes
        isEqual &= compareAttributes(elem1.attributes(), elem2.attributes(), path, differences);

        return isEqual;
    }

    private static boolean compareAttributes(final Attributes attrs1, final Attributes attrs2, final String path, final List<String> differences) {
        boolean isEqual = true;

        if (attrs1.size() != attrs2.size()) {
            differences.add(path + ": Different number of attributes - expected " + attrs1.size() + " but was " + attrs2.size());
            isEqual = false;
        }

        // Check all attributes in expected
        for (final Attribute attr1 : attrs1) {
            final String key = attr1.getKey();
            if (!attrs2.hasKey(key)) {
                differences.add(path + ": Missing attribute '" + key + "'");
                isEqual = false;
            }
            else {
                final String val1 = attr1.getValue();
                final String val2 = attrs2.get(key);
                if (!Objects.equals(val1, val2)) {
                    differences.add(path + ": Attribute '" + key + "' expected '" + val1 + "' but was '" + val2 + "'");
                    isEqual = false;
                }
            }
        }

        // Check for extra attributes in actual
        for (final Attribute attr2 : attrs2) {
            final String key = attr2.getKey();
            if (!attrs1.hasKey(key)) {
                differences.add(path + ": Unexpected attribute '" + key + "' with value '" + attr2.getValue() + "'");
                isEqual = false;
            }
        }

        return isEqual;
    }

    private static boolean compareTextNodes(final TextNode text1, final TextNode text2, final String path, final List<String> differences) {
        final String content1 = text1.text();
        final String content2 = text2.text();

        if (!Objects.equals(content1, content2)) {
            differences.add(path + ": Text content differs - expected '" + content1 + "' but was '" + content2 + "'");
            return false;
        }

        return true;
    }

    private static boolean compareComments(final Comment comment1, final Comment comment2, final String path, final List<String> differences) {
        final String data1 = comment1.getData();
        final String data2 = comment2.getData();

        if (!Objects.equals(data1, data2)) {
            differences.add(path + ": Comment data differs - expected '" + data1 + "' but was '" + data2 + "'");
            return false;
        }

        return true;
    }

    private static boolean compareDataNodes(final DataNode data1, final DataNode data2, final String path, final List<String> differences) {
        final String wholeData1 = data1.getWholeData();
        final String wholeData2 = data2.getWholeData();

        if (!Objects.equals(wholeData1, wholeData2)) {
            differences.add(path + ": Data node content differs - expected '" + wholeData1 + "' but was '" + wholeData2 + "'");
            return false;
        }

        return true;
    }

    private static boolean compareDocumentTypes(final DocumentType docType1, final DocumentType docType2, final String path, final List<String> differences) {
        boolean isEqual = true;

        if (!Objects.equals(docType1.name(), docType2.name())) {
            differences.add(path + ": DOCTYPE names differ - expected '" + docType1.name() + "' but was '" + docType2.name() + "'");
            isEqual = false;
        }

        if (!Objects.equals(docType1.publicId(), docType2.publicId())) {
            differences.add(path + ": DOCTYPE public IDs differ - expected '" + docType1.publicId() + "' but was '" + docType2.publicId() + "'");
            isEqual = false;
        }

        if (!Objects.equals(docType1.systemId(), docType2.systemId())) {
            differences.add(path + ": DOCTYPE system IDs differ - expected '" + docType1.systemId() + "' but was '" + docType2.systemId() + "'");
            isEqual = false;
        }

        return isEqual;
    }

    private static boolean compareChildren(final Node node1, final Node node2, final String path, final List<String> differences) {
        final List<Node> children1 = node1.childNodes();
        final List<Node> children2 = node2.childNodes();

        if (children1.size() != children2.size()) {
            differences.add(path + ": Different number of children - expected " + children1.size() + " but was " + children2.size());
            return false;
        }

        boolean isEqual = true;
        for (int i = 0; i < children1.size(); i++) {
            final String childPath = path.isEmpty() ? "[" + i + "]" : path + "[" + i + "]";
            isEqual &= compareNodes(children1.get(i), children2.get(i), childPath, differences);
        }

        return isEqual;
    }
}
