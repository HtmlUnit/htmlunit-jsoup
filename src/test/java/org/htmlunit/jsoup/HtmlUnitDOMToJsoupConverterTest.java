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
                "<html></html>";
        testDocument(html);
        testElement(html);
    }

    @Test
    public void bodyWithText() throws Exception {
        final String html =
                "<html><body>HtmlUnit</body></html>";
        testDocument(html);
        testElement(html);
    }

    @Test
    public void bodyWithParagraph() throws Exception {
        final String html =
                "<html><body><p>HtmlUnit</p></body></html>";
        testDocument(html);
        testElement(html);
    }

    @Test
    public void bodyWithTitle() throws Exception {
        final String html =
                "<html><body><h1>HtmlUnit</h1></body></html>";
        testDocument(html);
        testElement(html);
    }

    @Test
    public void uppercaseTags() throws Exception {
        final String html =
                "<HTML><BODY><H1>HtmlUnit</H1></BODY></HTML>";
        testDocument(html);
        testElement(html);
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
                "<html><body><p><b>HtmlUnit</b> <span><span>is</span> a</span> <span>gre<i>a</i>t</span> <span>framwork</span>.</p></body></html>";
        testDocument(html);
        testElement(html);
    }

    private void testDocument(final String html) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convert(page);

            final Node jsoupNode = Jsoup.parse(html);

            JsoupAssertions.assertNodesEqual(jsoupNode, htmlunitNode);
        }
    }

    private void testElement(final String html) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convert(page.getDocumentElement());

            final Node jsoupNode = Jsoup.parse(html);

            Assertions.assertEquals(1, jsoupNode.childNodes().size());
            JsoupAssertions.assertNodesEqual(jsoupNode.childNode(0), htmlunitNode);
        }
    }
}
