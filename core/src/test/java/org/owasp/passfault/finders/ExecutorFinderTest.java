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
package org.owasp.passfault.finders;

import org.junit.BeforeClass;
import org.junit.Test;
import org.owasp.passfault.*;
import org.owasp.passfault.dictionary.DictionaryPatternsFinder;
import org.owasp.passfault.dictionary.ExactWordStrategy;
import org.owasp.passfault.dictionary.FileDictionary;
import org.owasp.passfault.dictionary.TestWords;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class ExecutorFinderTest {

  private static CompositeFinder finder;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    FileDictionary dictionary = FileDictionary.newInstance(TestWords.getTestFile(), "tiny-lower");
    DictionaryPatternsFinder dictionaryFinder = new DictionaryPatternsFinder(dictionary, new ExactWordStrategy());
    LinkedList<PatternFinder> l = new LinkedList<>();
    l.add(dictionaryFinder);
    finder = new ExecutorFinder(l);
  }

  @Test
  public void findWord() throws Exception {
    System.out.println("findWord");
    PasswordAnalysis p = new PasswordAnalysis("wisp");
    finder.blockingAnalyze(p);
    assertEquals(1, p.getPossiblePatternCount());
  }

  @Test
  public void garbageInFront() throws Exception {
    System.out.println("findWord_garbageinfront");
    PasswordAnalysis p = new PasswordAnalysis("1234wisp");
    finder.blockingAnalyze(p);

    assertEquals(1, p.getPossiblePatternCount());
  }

  @Test
  public void garbageInBack() throws Exception {

    PasswordAnalysis p = new PasswordAnalysis("wisp1234");
    finder.blockingAnalyze(p);
    assertEquals(1, p.getPossiblePatternCount());
  }

  @Test
  public void findNonWord() throws Exception {
    PasswordAnalysis p = new PasswordAnalysis("qqq");
    finder.blockingAnalyze(p);
    assertEquals(0, p.getPossiblePatternCount());
  }

  @Test
  public void findMultiWords() throws Exception {
    PasswordAnalysis p = new PasswordAnalysis("wispwisp");
    finder.blockingAnalyze(p);
    assertEquals(2, p.getPossiblePatternCount());
  }

  @Test
  public void findWordWithMulti() throws Exception {
    MockPasswordResults p = new MockPasswordResults("password");
    finder.blockingAnalyze(p);
    assertEquals(6, p.getPossiblePatternCount());
    for (PasswordPattern pattern : p.getFoundPatterns()) {
      System.out.println(pattern.getMatchString());
    }
  }

  @Test
  public void findWordWithMultiUpper() throws Exception {
    MockPasswordResults p = new MockPasswordResults("Password");
    finder.blockingAnalyze(p);
    assertEquals(6, p.getPossiblePatternCount());
  }

  @Test
  public void findWithMultiplePasswords() throws Exception {
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
