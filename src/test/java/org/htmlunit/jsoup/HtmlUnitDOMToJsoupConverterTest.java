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

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.jsoup.utils.JsoupAssertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HtmlUnitDOMToJsoupConverterTest {

    @Test
    public void convertNull() throws Exception {
        final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
        final Node htmlunitNode = converter.convert(null);

        Assertions.assertNull(htmlunitNode);
    }

    @Test
    public void empty() throws Exception {
        final String html =
                "<html id='t'></html>";
        testDocument(html);
        testElement(html);

        testElement(html, "t");
    }

    @Test
    public void bodyWithText() throws Exception {
        final String html =
                "<html id='t1'><body id='t2'>HtmlUnit</body></html>";
        testDocument(html);
        testElement(html);

        testElement(html, "t1");
        testElement(html, "t2");
    }

    @Test
    public void bodyWithParagraph() throws Exception {
        final String html =
                "<html><body id='t1'><p id='t2'>HtmlUnit</p></body></html>";
        testDocument(html);
        testElement(html);

        testElement(html, "t1");
        testElement(html, "t2");
    }

    @Test
    public void bodyWithTitle() throws Exception {
        final String html =
                "<html><body id='t1'><h1 id='t2'>HtmlUnit</h1></body></html>";
        testDocument(html);
        testElement(html);

        testElement(html, "t1");
        testElement(html, "t2");
    }

    @Test
    public void uppercaseTags() throws Exception {
        final String html =
                "<HTML><BODY id='t1'><H1 id='t2'>HtmlUnit</H1></BODY></HTML>";
        testDocument(html);
        testElement(html);

        testElement(html, "t1");
        testElement(html, "t2");
    }

    @Test
    public void mixedcaseTags() throws Exception {
        final String html =
                "<HTML><BodY><h1>HtmlUnit</H1></BODY></html>";
        testDocument(html);
        testElement(html);
    }

    @Test
    public void formattedText() throws Exception {
        final String html =
                "<html><body><p id='t1'><b id='t2'>HtmlUnit</b> <span id='t3'><span id='t4'>is</span> a</span> <span>gre<i>a</i>t</span> <span id='t5'>framwork</span>.</p></body></html>";
        testDocument(html);
        testElement(html);

        testElement(html, "t1");
        testElement(html, "t2");
        testElement(html, "t3");
        testElement(html, "t4");
        testElement(html, "t5");
    }

    private static void testDocument(final String html) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convert(page);

            final Node jsoupNode = Jsoup.parse(html);

            JsoupAssertions.assertNodesEqual(jsoupNode, htmlunitNode);
        }
    }

    private static void testElement(final String html) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convert(page.getDocumentElement());

            final Node jsoupNode = Jsoup.parse(html);

            Assertions.assertEquals(1, jsoupNode.childNodes().size());
            JsoupAssertions.assertNodesEqual(jsoupNode.childNode(0), htmlunitNode);
        }
    }

    private static void testElement(final String html, final String id) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final org.w3c.dom.Node htmlunitElementById = page.getElementById(id);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convertWholeTree(htmlunitElementById);

            final Document jsoupNode = Jsoup.parse(html);
            final Node jsoupNodeById = jsoupNode.selectFirst("#" + id);

            JsoupAssertions.assertNodesEqual(jsoupNodeById, htmlunitNode);
            JsoupAssertions.assertNodesEqual(jsoupNode, htmlunitNode.root());
        }
    }
}
