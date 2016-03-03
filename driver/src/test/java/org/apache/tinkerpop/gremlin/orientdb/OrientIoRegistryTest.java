package org.apache.tinkerpop.gremlin.orientdb;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.util.List;

import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONIo;
import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;

public class OrientIoRegistryTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        @SuppressWarnings("rawtypes")
        List<Pair<Class, Object>> modules = OrientIoRegistry.getInstance().find(GraphSONIo.class);
        objectMapper = new ObjectMapper();
        modules.forEach(module -> objectMapper.registerModule((Module) module.getValue1()));
    }

    @Test
    public void serializeORecordID747() throws Exception {
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, new ORecordId(7, 47));

        String result = sw.toString();
        assertThat(result, equalTo("{\"clusterId\":7,\"clusterPosition\":47}"));
    }

    @Test
    public void serializeORecordID00() throws Exception {
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, new ORecordId(0, 0));

        String result = sw.toString();
        assertThat(result, equalTo("{\"clusterId\":0,\"clusterPosition\":0}"));
    }

    @Test
    public void serializeORecordIDMaxMax() throws Exception {
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, new ORecordId(ORID.CLUSTER_MAX, Long.MAX_VALUE));

        String result = sw.toString();
        assertThat(result, equalTo("{\"clusterId\":32767,\"clusterPosition\":9223372036854775807}"));
    }

    @Test
    public void serializeVertex() throws Exception {
        OrientGraph graph = new OrientGraphFactory("memory:serializer", "root", "root").getNoTx();

        graph.addVertex();
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, new ORecordId(ORID.CLUSTER_MAX, Long.MAX_VALUE));

        String result = sw.toString();
        assertThat(result, equalTo("{\"clusterId\":32767,\"clusterPosition\":9223372036854775807}"));
    }

}
