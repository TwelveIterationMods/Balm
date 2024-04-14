package net.blay09.mods.balm.config;

import net.blay09.mods.balm.api.config.*;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Config("balm")
public class ExampleConfigData implements BalmConfigData {

    public enum ExampleEnum {
        Hello,
        World
    }

    @Comment("This is an example boolean property")
    public boolean exampleBoolean = true;
    public int exampleInt = 42;
    public String exampleString = "Hello World";
    public String exampleMultilineString = "Hello World";
    public ExampleEnum exampleEnum = ExampleEnum.Hello;
    @Synced
    @ExpectedType(String.class)
    public List<String> exampleStringList = Arrays.asList("Hello", "World");
    @Synced
    @ExpectedType(ResourceLocation.class)
    public Set<ResourceLocation> exampleResourceLocationSet = Set.of(new ResourceLocation("dirt"), new ResourceLocation("diamond"));
    @ExpectedType(Integer.class)
    public List<Integer> exampleIntList = Arrays.asList(12, 24);
    @ExpectedType(ExampleEnum.class)
    public List<ExampleEnum> exampleEnumList = Arrays.asList(ExampleEnum.Hello, ExampleEnum.World);

    @Comment("This is an example category")
    public ExampleCategory exampleCategory = new ExampleCategory();

    public static class ExampleCategory {
        @Comment("This is an example string inside a category")
        public String innerField = "I am inside";
        public float exampleFloat = 42.84f;
    }
}
