package com.internship.tool.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    // ✅ Concrete subclass because BaseEntity is abstract
    static class TestEntity extends BaseEntity {}

    @Test
    void testBaseEntityFieldsUsingReflection() throws Exception {

        TestEntity entity = new TestEntity();

        LocalDateTime now = LocalDateTime.now();

        // Access private fields via reflection
        Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
        Field updatedAtField = BaseEntity.class.getDeclaredField("updatedAt");

        createdAtField.setAccessible(true);
        updatedAtField.setAccessible(true);

        createdAtField.set(entity, now);
        updatedAtField.set(entity, now);

        assertEquals(now, createdAtField.get(entity));
        assertEquals(now, updatedAtField.get(entity));
    }
}