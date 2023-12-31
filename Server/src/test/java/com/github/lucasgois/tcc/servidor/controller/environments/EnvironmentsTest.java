package com.github.lucasgois.tcc.servidor.controller.environments;


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

import static com.github.lucasgois.tcc.servidor.controller.Locations.ENVIRONMENTS;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Environments test")
@SpringBootTest
@AutoConfigureMockMvc
class EnvironmentsTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private Map<String, Object> environment;

    @Test
    @DisplayName("Not Found test")
    void notFoundTest() throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + ENVIRONMENTS + "/" + "91b037d1-f04d-4b9c-b8e5-f1f8ae4df1dd"));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Unprocessable Entity test")
    void unprocessableEntityTest() throws Exception {
        final ResultActions result = mockMvc.perform(post("/" + ENVIRONMENTS)
                                                             .contentType("application/json")
                                                             .content("{ \"invalid_field\": \"invalid_field_value\"}"));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Bad Request Test")
    void badRequestTest() throws Exception {
        final ResultActions result = mockMvc.perform(post("/" + ENVIRONMENTS)
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

        doPut(location);

        final Map<?, ?> bodyAfterPut = doSelect(location);

        assertEquals(bodyAfterPost.get("environment_id"), bodyAfterPut.get("environment_id"));
        assertNotEquals(bodyAfterPost.get("environment_name"), bodyAfterPut.get("environment_name"));
        assertEquals(bodyAfterPost.get("environment_created_at"), bodyAfterPut.get("environment_created_at"));
        assertNotEquals(bodyAfterPost.get("environment_updated_at"), bodyAfterPut.get("environment_updated_at"));

        doDelete(location);

        final int sizeAfterDelete = doSelectAll();

        assertNotEquals(sizeAfterPost, sizeAfterDelete);
        assertEquals(sizeInitial, sizeAfterDelete);
    }


    @NotNull
    private String doPost() throws Exception {
        environment = new HashMap<>();
        environment.put("environment_name", "Environment Name");

        final ResultActions result = mockMvc.perform(post("/" + ENVIRONMENTS)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(environment)));

        final MvcResult mvcResult = result.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        final String location = mvcResult.getResponse().getHeader("Location");

        return requireNonNull(location);
    }

    private int doSelectAll() throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + ENVIRONMENTS));

        final MvcResult mvcResult = result.andExpect(status().isOk())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        final List<?> list = mapper.readValue(content, List.class);

        return list.size();
    }

    private Map<?, ?> doSelect(final String location) throws Exception {
        final ResultActions result = mockMvc.perform(get("/" + location));

        final MvcResult mvcResult = result.andExpect(status().isOk())
                .andExpect(jsonPath("$.environment_name", is(environment.get("environment_name"))))
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        return mapper.readValue(content, Map.class);
    }


    private void doPut(final String location) throws Exception {
        environment = new HashMap<>();
        environment.put("environment_name", "Environment Name Updated");

        final ResultActions result = mockMvc.perform(put("/" + location)
                                                             .contentType("application/json")
                                                             .content(mapper.writeValueAsBytes(environment)));

        result.andExpect(status().isOk());
    }

    private void doDelete(final String location) throws Exception {
        final ResultActions result = mockMvc.perform(delete("/" + location));

        result.andExpect(status().isOk());
    }
}