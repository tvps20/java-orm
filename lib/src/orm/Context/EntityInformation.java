package orm.Context;

import orm.Attributes.*;
import orm.Attributes.Integer;
import utils.ReflectionExtensions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class EntityInformation {

    private Class entity;

    public EntityInformation(Class entity) {
        this.entity = entity;
    }

    public String getTableName() {
        return entity.getSimpleName();
    }

    public EntityPropertyInformation[] getPropertiesInformations() {
        ArrayList<EntityPropertyInformation> list = new ArrayList<>();

        for (Field field : ReflectionExtensions.getAllFields(new ArrayList<Field>(), entity)) {

            field.setAccessible(true);
            String fieldName = field.getName();
            String constraint = "";

            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                   constraint += " " + handleTypeAnnotation(annotation);
            }

            list.add(new EntityPropertyInformation(fieldName, constraint));
        }

        EntityPropertyInformation[] array = new EntityPropertyInformation[list.size()];
        return list.toArray(array);
    }

    public EntityKeyInformation[] getKeysInformations() {
        ArrayList<EntityKeyInformation> list = new ArrayList<>();

        for (Field field : ReflectionExtensions.getAllFields(new ArrayList<Field>(), entity)) {

            field.setAccessible(true);
            String fieldName = field.getName();
            String instruction = "";

            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                instruction += " " + handleKeyAnnotation(annotation, fieldName, entity.getSimpleName());
            }

            list.add(new EntityKeyInformation(fieldName, instruction));
        }

        EntityKeyInformation[] array = new EntityKeyInformation[list.size()];
        return list.toArray(array);
    }

    private String handleKeyAnnotation(Annotation annotation, String name, String table) {
        if (annotation instanceof Key) {
            Key attribute = (Key) annotation;
            return attribute.instruction();
        } else if (annotation instanceof Unique) {
            Unique attribute = (Unique) annotation;
            return  attribute.instruction().replace("$", name);
        } else if (annotation instanceof ForeingKey) {
            ForeingKey attribute = (ForeingKey) annotation;
            String key = generateUniqueKey(name, table);
            return  attribute.instruction().replace("%", key).replace("$", name).replace("&", attribute.table());
        }

        return "";
    }

    private String handleTypeAnnotation(Annotation annotation) {

        if (annotation instanceof AutoIncrement) {
            AutoIncrement attribute = (AutoIncrement) annotation;
            return attribute.constrange();
        } else if (annotation instanceof DateTime) {
            DateTime attribute = (DateTime) annotation;
            return  attribute.constrange();
        } else if (annotation instanceof Decimal) {
            Decimal attribute = (Decimal) annotation;
            return  attribute.constrange();
        }  else if (annotation instanceof Integer) {
            Integer attribute = (Integer) annotation;
            return  attribute.constrange();
        }  else if (annotation instanceof Required) {
            Required attribute = (Required) annotation;
            return  attribute.constrange();
        }  else if (annotation instanceof Varchar) {
            Varchar attribute = (Varchar) annotation;
            return  attribute.constrange();
        } else if (annotation instanceof ForeingKey) {
            ForeingKey attribute = (ForeingKey) annotation;
            return attribute.constrange();
        } else if (annotation instanceof AllowNull) {
            AllowNull attribute = (AllowNull) annotation;
            return attribute.constrange();
        } else if (annotation instanceof Key) {
            Key attribute = (Key) annotation;
            return attribute.constrange();
        }

        return "";
    }

    private String generateUniqueKey(String name, String table) {
        return table + "_" + name;
    }
}
