package com.terracottatech.qa.angela.common.topology;

import com.terracottatech.qa.angela.common.distribution.Distribution;
import com.terracottatech.qa.angela.common.tcconfig.ServerSymbolicName;
import com.terracottatech.qa.angela.common.tcconfig.TcConfig;
import com.terracottatech.qa.angela.common.tcconfig.TerracottaServer;
import com.terracottatech.qa.angela.common.tcconfig.TsaConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds the test environment topology:
 * - Tc Config that represents the Terracotta cluster
 * - List of nodes where the test instances will run
 * - Version of the Terracotta installation
 */

public class Topology {
  private final Distribution distribution;
  private final List<TcConfig> tcConfigs;
  private final boolean netDisruptionEnabled;

  public Topology(Distribution distribution, TsaConfig tsaConfig) {
    this(distribution, false, tsaConfig.buildTcConfigs());
  }

public Topology(Distribution distribution, boolean netDisruptionEnabled, TsaConfig tsaConfig) {
    this(distribution, netDisruptionEnabled, tsaConfig.buildTcConfigs());
  }


  public Topology(Distribution distribution, TcConfig tcConfig, TcConfig... tcConfigs) {
    this(distribution, false, tcConfig, tcConfigs);
  }

  public Topology(Distribution distribution, boolean netDisruptionEnabled, TcConfig tcConfig, TcConfig... tcConfigs) {
    this(distribution, netDisruptionEnabled, mergeTcConfigs(tcConfig, tcConfigs));
  }

  private static List<TcConfig> mergeTcConfigs(final TcConfig tcConfig, final TcConfig[] tcConfigs) {
    final ArrayList<TcConfig> configs = new ArrayList<>();
    configs.add(tcConfig);
    configs.addAll(Arrays.asList(tcConfigs));
    return configs;
  }

  private Topology(Distribution distribution, boolean netDisruptionEnabled, List<TcConfig> tcConfigs) {
    this.distribution = distribution;
    this.netDisruptionEnabled = netDisruptionEnabled;
    this.tcConfigs = tcConfigs;
    checkConfigsHaveNoSymbolicNameDuplicate();
    if (netDisruptionEnabled) {
      for (TcConfig cfg : this.tcConfigs) {
        cfg.createOrUpdateTcProperty("topology.validate", "false");
        cfg.createOrUpdateTcProperty("l1redirect.enabled", "false");
      }
    }
  }

  private void checkConfigsHaveNoSymbolicNameDuplicate() {
    Set<ServerSymbolicName> names = new HashSet<>();
    for (TcConfig tcConfig : tcConfigs) {
      tcConfig.getServers().forEach(server -> {
        ServerSymbolicName serverSymbolicName = server.getServerSymbolicName();
        if (names.contains(serverSymbolicName)) {
          throw new IllegalArgumentException("Duplicate name found in TC configs : " + server);
        } else {
          names.add(serverSymbolicName);
        }
      });
    }
  }

  public Collection<TerracottaServer> getServers() {
    List<TerracottaServer> servers = new ArrayList<>();
    for (TcConfig tcConfig : this.tcConfigs) {
      servers.addAll(tcConfig.getServers());
    }
    return servers;
  }

  public TerracottaServer findServer(int stripeId, int serverIndex) {
    if (stripeId >= tcConfigs.size()) {
      throw new IllegalArgumentException("No such stripe #" + stripeId + " (there are: " + tcConfigs.size() + ")");
    }
    List<TerracottaServer> servers = tcConfigs.get(stripeId).getServers();
    if (serverIndex >= servers.size()) {
      throw new IllegalArgumentException("No such server #" + serverIndex + " (there are: " + servers.size() + " in stripe " + stripeId + ")");
    }
    return servers.get(serverIndex);
  }

  public TcConfig findTcConfigOf(ServerSymbolicName serverSymbolicName) {
    for (TcConfig tcConfig : this.tcConfigs) {
      for (TerracottaServer server : tcConfig.getServers()) {
        if (server.getServerSymbolicName().equals(serverSymbolicName)) {
          return tcConfig;
        }
      }
    }
    return null;
  }

  public int findStripeIdOf(ServerSymbolicName serverSymbolicName) {
    for (int i = 0; i < tcConfigs.size(); i++) {
      TcConfig tcConfig = tcConfigs.get(i);
      for (TerracottaServer server : tcConfig.getServers()) {
        if (server.getServerSymbolicName().equals(serverSymbolicName)) {
          return i;
        }
      }
    }
    return -1;
  }

  public List<TcConfig> getTcConfigs() {
    return this.tcConfigs;
  }

  public Collection<String> getServersHostnames() {
    return getServers().stream().map(TerracottaServer::getHostname).collect(Collectors.toList());
  }

  public LicenseType getLicenseType() {
    return distribution.getLicenseType();
  }

  public boolean isNetDisruptionEnabled() {
    return netDisruptionEnabled;
  }

  public Distribution getDistribution() {
    return distribution;
  }

  @Override
  public String toString() {
    return "Topology{" +
           "distribution=" + distribution +
           ", tcConfigs=" + tcConfigs +
           ", netDisruptionEnabled=" + netDisruptionEnabled +
           '}';
  }
}
