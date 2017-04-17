package io.galeb.router.tests.hostselectors;

import io.galeb.router.client.ExtendedLoadBalancingProxyClient.Host;
import io.galeb.router.client.hostselectors.LeastConnWithRRHostSelector;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LeastConnWithRRHostSelectorTest extends AbstractHostSelectorTest {

    private final LeastConnWithRRHostSelector leastConnWithRRHostSelector = new LeastConnWithRRHostSelector();

    @Test
    public void testSelectHost() throws Exception {
        for (int retry = 1; retry <= NUM_RETRIES; retry++) {
            int hostsLength = new Random().nextInt(NUM_HOSTS);
            final long limit = (int) Math.ceil((float) hostsLength * leastConnWithRRHostSelector.getCuttingLine());
            IntStream.range(0, hostsLength - 1).forEach(x -> {
                final Host[] newHosts = Arrays.copyOf(hosts, hostsLength);
                long result = leastConnWithRRHostSelector.selectHost(newHosts, commonExchange);
                assertThat(result, equalTo(x % limit));
            });
            leastConnWithRRHostSelector.reset();
        }
    }
} 