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
package org.terracotta.angela.agent.com;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Mathieu Carbou
 */
public class LocalAgentGroup extends AgentGroup {
  private static final long serialVersionUID = 1L;

  LocalAgentGroup(UUID group, AgentID local) {
    super(group, local);
  }

  @Override
  public Collection<AgentID> getAllAgents() {
    return Collections.singletonList(getLocalAgentID());
  }
}
