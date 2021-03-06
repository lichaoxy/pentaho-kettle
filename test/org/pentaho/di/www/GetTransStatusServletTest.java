/*! ******************************************************************************
*
* Pentaho Data Integration
*
* Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
*
*******************************************************************************
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
******************************************************************************/

package org.pentaho.di.www;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.gui.Point;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class GetTransStatusServletTest {
  private TransformationMap mockTransformationMap;

  private GetTransStatusServlet getTransStatusServlet;

  @Before
  public void setup() {
    mockTransformationMap = mock(TransformationMap.class);
    getTransStatusServlet = new GetTransStatusServlet(mockTransformationMap);
  }

  @Test
  public void testGetTransStatusServletEscapesHtmlWhenTransNotFound() throws ServletException, IOException {
    HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);

    StringWriter out = new StringWriter();
    PrintWriter printWriter = new PrintWriter(out);

    when(mockHttpServletRequest.getContextPath()).thenReturn(GetTransStatusServlet.CONTEXT_PATH);
    when(mockHttpServletRequest.getParameter(anyString())).thenReturn(ServletTestUtils.BAD_STRING);
    when(mockHttpServletResponse.getWriter()).thenReturn(printWriter);

    getTransStatusServlet.doGet(mockHttpServletRequest, mockHttpServletResponse);
    assertFalse(ServletTestUtils.hasBadText(ServletTestUtils.getInsideOfTag("H1", out.toString())));
  }

  @Test
  public void testGetTransStatusServletEscapesHtmlWhenTransFound() throws ServletException, IOException {
    KettleLogStore.init();
    HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);
    Trans mockTrans = mock(Trans.class);
    TransMeta mockTransMeta = mock(TransMeta.class);
    LogChannelInterface mockChannelInterface = mock(LogChannelInterface.class);
    StringWriter out = new StringWriter();
    PrintWriter printWriter = new PrintWriter(out);

    when(mockHttpServletRequest.getContextPath()).thenReturn(GetTransStatusServlet.CONTEXT_PATH);
    when(mockHttpServletRequest.getParameter(anyString())).thenReturn(ServletTestUtils.BAD_STRING);
    when(mockHttpServletResponse.getWriter()).thenReturn(printWriter);
    when(mockTransformationMap.getTransformation(any(CarteObjectEntry.class))).thenReturn(mockTrans);
    when(mockTrans.getLogChannel()).thenReturn(mockChannelInterface);
    when(mockTrans.getTransMeta()).thenReturn(mockTransMeta);
    when(mockTransMeta.getMaximum()).thenReturn(new Point(10, 10));

    getTransStatusServlet.doGet(mockHttpServletRequest, mockHttpServletResponse);
    assertFalse(out.toString().contains(ServletTestUtils.BAD_STRING));
  }
}
