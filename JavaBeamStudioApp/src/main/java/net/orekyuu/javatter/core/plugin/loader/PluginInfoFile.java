package net.orekyuu.javatter.core.plugin.loader;

class PluginInfoFile {
    private String name;
    private String id;
    private String version;
    private String entryPoint;
    private String requireVersion;

    String getName() {
        return name;
    }

    String getId() {
        return id;
    }

    String getVersion() {
        return version;
    }

    String getEntryPoint() {
        return entryPoint;
    }

    String getRequireVersion() {
        return requireVersion;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PluginInfoFile{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", entryPoint='").append(entryPoint).append('\'');
        sb.append(", requireVersion='").append(requireVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
