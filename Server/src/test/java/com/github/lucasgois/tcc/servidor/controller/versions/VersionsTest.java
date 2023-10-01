package com.github.lucasgois.tcc.servidor.controller.versions;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.lucasgois.tcc.servidor.controller.Locations.*;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Versions test")
@SpringBootTest
@AutoConfigureMockMvc
class VersionsTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private Map<String, Object> version;
    private String moduleId;
    private String environmentId;

    @Test
    @DisplayName("Not found test")
    void notFoundTest() throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + VERSIONS + "/" + "91b037d1-f04d-4b9c-b8e5-f1f8ae4df1dd"));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Unprocessable Entity test")
    void unprocessableEntityTest() throws Exception {
        final ResultActions result = mockMvc.perform(post("/" + VERSIONS)
                                                             .contentType("application/json")
                                                             .content("{ \"invalid_field\": \"invalid_field_value\"}"));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Bad Request Test")
    void badRequestTest() throws Exception {
        final ResultActions result = mockMvc.perform(post("/" + VERSIONS)
                                                             .contentType("application/json")
                                                             .content(""));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("All methods test")
    void test() throws Exception {
        final int sizeInitial = doSelectAll();

        final String location = doPost();

        final int sizeAfterPost = doSelectAll();

        assertNotEquals(sizeInitial, sizeAfterPost);

        final Map<?, ?> bodyAfterPost = doSelect(location);

        doDeleteModule();
        doDeleteEnvironment();

        doPut(location);

        final Map<?, ?> bodyAfterPut = doSelect(location);

        assertEquals(bodyAfterPost.get("version_id"), bodyAfterPut.get("version_id"));
        assertNotEquals(bodyAfterPost.get("version_name"), bodyAfterPut.get("version_name"));
        assertEquals(bodyAfterPost.get("version_created_at"), bodyAfterPut.get("version_created_at"));
        assertNotEquals(bodyAfterPost.get("version_updated_at"), bodyAfterPut.get("version_updated_at"));

        doDelete(location);

        final int sizeAfterDelete = doSelectAll();

        assertNotEquals(sizeAfterPost, sizeAfterDelete);
        assertEquals(sizeInitial, sizeAfterDelete);

        doDeleteModule();
        doDeleteEnvironment();
    }


    @NotNull
    private String doPost() throws Exception {
        version = new HashMap<>();
        version.put("version_name", "version created");
        version.put("module_id", getModuleId());
        version.put("environment_id", getEnvironmentId());

        final ResultActions result = mockMvc.perform(post("/" + VERSIONS)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(version)));

        final MvcResult mvcResult = result.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        final String location = mvcResult.getResponse().getHeader("Location");

        return requireNonNull(location);
    }

    private int doSelectAll() throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + VERSIONS));

        final MvcResult mvcResult = result.andExpect(status().isOk())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        final List<?> list = mapper.readValue(content, List.class);

        return list.size();
    }

    private Map<?, ?> doSelect(final String location) throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + location));

        final MvcResult mvcResult = result.andExpect(status().isOk())
                .andExpect(jsonPath("$.version_name", is(version.get("version_name"))))
                .andExpect(jsonPath("$.environment_id", is(version.get("environment_id"))))
                .andExpect(jsonPath("$.module_id", is(version.get("module_id"))))
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        return mapper.readValue(content, Map.class);
    }


    private void doPut(final String location) throws Exception {
        version = new HashMap<>();
        version.put("version_name", "version updated");
        version.put("environment_id", getEnvironmentId());
        version.put("module_id", getModuleId());

        final ResultActions result = mockMvc.perform(put("/" + location)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(version)));

        result.andExpect(status().isOk());
    }

    private void doDelete(final String location) throws Exception {
        final ResultActions result = mockMvc.perform(delete("/" + location));

        result.andExpect(status().isOk());
    }

    String getModuleId() throws Exception {

        if (moduleId != null) {
            return moduleId;
        }

        final Map<String, String> module = new HashMap<>();
        module.put("module_name", "Module Version Test");

        final ResultActions result = mockMvc.perform(post("/" + MODULES)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(module)));

        final MvcResult mvcResult = result.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        final String location = requireNonNull(mvcResult.getResponse().getHeader("Location"));

        moduleId = location.substring(location.indexOf('/') + 1);

        return moduleId;
    }

    private void doDeleteModule() throws Exception {
        final ResultActions result = mockMvc.perform(delete('/' + MODULES + '/' + getModuleId()));
        result.andExpect(status().isOk());
        moduleId = null;
    }

    String getEnvironmentId() throws Exception {

        if (environmentId != null) {
            return environmentId;
        }

        final Map<String, String> module = new HashMap<>();
        module.put("environment_name", "Environment Version Test");

        final ResultActions result = mockMvc.perform(post("/" + ENVIRONMENTS)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(module)));

        final MvcResult mvcResult = result.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        final String location = requireNonNull(mvcResult.getResponse().getHeader("Location"));

        environmentId = location.substring(location.indexOf('/') + 1);

        return environmentId;
    }

    private void doDeleteEnvironment() throws Exception {
        final ResultActions result = mockMvc.perform(delete('/' + ENVIRONMENTS + '/' + getEnvironmentId()));
        result.andExpect(status().isOk());

        environmentId = null;
    }
}