/* ©Copyright 2011 Cameron Morris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.passfault;

import java.io.File;
import com.passfault.dictionary.*;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.*;

public class ParallelPatternsFinderTest {

  private static ParallelFinder finder;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    System.out.println("current dir=" + new File(".").getAbsoluteFile());
    FileDictionary dictionary = FileDictionary.newInstance("./test/tiny-lower.words", "tiny-lower");
    DictionaryPatternsFinder dictionaryFinder = new DictionaryPatternsFinder(dictionary, new ExactWordStrategy());
    LinkedList<PatternFinder> l = new LinkedList<PatternFinder>();
    l.add(dictionaryFinder);
    finder = new ParallelFinder(l);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void findWord() throws Exception {
    System.out.println("findWord");
    PasswordAnalysis p = new PasswordAnalysis("wisp");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(1, p.getPossiblePatternCount());

  }

  @Test
  public void findWord_garbageinfront() throws Exception {
    System.out.println("findWord_garbageinfront");
    PasswordAnalysis p = new PasswordAnalysis("1234wisp");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(1, p.getPossiblePatternCount());
  }

  @Test
  public void findWord_garbageinback() throws Exception {

    System.out.println("findWord_garbageinback");
    PasswordAnalysis p = new PasswordAnalysis("wisp1234");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(1, p.getPossiblePatternCount());
  }

  @Test
  public void findNonWord() throws Exception {
    System.out.println("findNonWord");

    PasswordAnalysis p = new PasswordAnalysis("qqq");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(0, p.getPossiblePatternCount());
  }

  @Test
  public void findMultiWords() throws Exception {
    System.out.println("findMultiWords");
    PasswordAnalysis p = new PasswordAnalysis("wispwisp");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(2, p.getPossiblePatternCount());
  }

  @Test
  public void findWordWithMulti() throws Exception {
    System.out.println("findMultiWords");
    PasswordAnalysis p = new PasswordAnalysis("password");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(4, p.getPossiblePatternCount());
    assertEquals("password", p.calculateHighestProbablePatterns().path.get(0).getMatchString());
  }

  @Test
  public void findWordWithMultiUpper() throws Exception {
    System.out.println("findMultiWords");
    PasswordAnalysis p = new PasswordAnalysis("Password");
    finder.analyze(p);
    finder.waitForAnalysis(p);
    assertEquals(4, p.getPossiblePatternCount());
    assertEquals("Password", p.calculateHighestProbablePatterns().path.get(0).getMatchString());
  }

  @Test
  public void findWithMultiplePasswords() throws Exception {
    System.out.println("findWithMultipltPasswords");
    String passwords[] = {
      "password", "drowssap", "2pass$word", "3drowsap",
      "1234e34t%46", "what3ver", "djhfjgnt", "3e35cdF3f",
      "password", "drowssap", "2pass$word", "3drowsap",
      "1234e34t%46", "what3ver", "djhfjgnt", "3e35cdF3f",
      "password", "drowssap", "2pass$word", "3drowsap",
      "1234e34t%46", "what3ver", "djhfjgnt", "3e35cdF3f",
      "password", "drowssap", "2pass$word", "3drowsap",
      "1234e34t%46", "what3ver", "djhfjgnt", "3e35cdF3f",
      "password", "drowssap", "2pass$word", "3drowsap",
      "1234e34t%46", "what3ver", "djhfjgnt", "3e35cdF3f",};
    PasswordAnalysis analysis[] = new PasswordAnalysis[passwords.length];
    for (int i = 0; i < passwords.length; i++) {
      analysis[i] = new PasswordAnalysis(passwords[i]);
    }

    for (int i = 0; i < passwords.length; i++) {
      finder.analyze(analysis[i]);
    }

    for (int i = 0; i < passwords.length; i++) {
      finder.waitForAnalysis(analysis[i]);
    }
  }
}
