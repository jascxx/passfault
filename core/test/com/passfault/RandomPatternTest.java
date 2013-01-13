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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author cam
 */
public class RandomPatternTest {

  /**
   * Test of getRandomPattern method, of class RandomPattern.
   */
  @Test
  public void testGetRandomPattern() {
    System.out.println("getRandomPattern");
    CharSequence chars = "afaf";
    int start = 0;
    int length = 4;
    RandomPattern instance = new RandomPattern();
    PasswordPattern result = instance.getRandomPattern(chars, start, length);
    assertEquals(Math.pow(RandomPattern.RandomClasses.Latin.getSize(), 4), result.getCost(), .1);

  }
}
