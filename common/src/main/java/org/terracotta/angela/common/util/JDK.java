/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terracotta.angela.common.util;

public class JDK {

  private final UniversalPath home;
  private final String version;
  private final String vendor;

  public JDK(UniversalPath home, String version, String vendor) {
    this.home = home;
    this.version = version;
    this.vendor = vendor;
  }

  public UniversalPath getHome() {
    return home;
  }

  public String getVersion() {
    return version;
  }

  public String getVendor() {
    return vendor;
  }

  @Override
  public String toString() {
    return "JDK{" +
        "home='" + home + '\'' +
        ", version='" + version + '\'' +
        ", vendor='" + vendor + '\'' +
        '}';
  }
}
