/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.common.jsonexplain.tez;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTezJsonParser {

  private TezJsonParser uut;

  private JSONParser parser = new JSONParser();

  @Before
  public void setUp() throws Exception {
    this.uut = new TezJsonParser();
  }

  @Test
  public void testExtractStagesAndPlans() throws Exception {
    String jsonString = "{\"STAGE DEPENDENCIES\":{\"s1\":{\"ROOT STAGE\":\"\"}," +
            "\"s2\":{\"DEPENDENT STAGES\":\"s1\"}},\"STAGE PLANS\":{}}";
    JSONObject input = (JSONObject) parser.parse(jsonString);

    uut.inputObject = input;
    uut.extractStagesAndPlans();

    assertEquals(2, uut.stages.size());
    assertEquals(1, uut.stages.get("s1").childStages.size());
    assertEquals("s2", uut.stages.get("s1").childStages.get(0).name);
    assertEquals(0, uut.stages.get("s2").childStages.size());
    assertEquals(0, uut.stages.get("s1").parentStages.size());
    assertEquals(1, uut.stages.get("s2").parentStages.size());
    assertEquals("s1", uut.stages.get("s2").parentStages.get(0).name);
  }

}