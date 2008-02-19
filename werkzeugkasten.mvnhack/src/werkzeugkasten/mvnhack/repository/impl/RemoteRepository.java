package werkzeugkasten.mvnhack.repository.impl;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import werkzeugkasten.common.util.UrlUtil;
import werkzeugkasten.mvnhack.Constants;
import werkzeugkasten.mvnhack.repository.Artifact;
import werkzeugkasten.mvnhack.repository.Repository;

public class RemoteRepository implements Repository {

	protected String baseUrl;

	protected ArtifactBuilder builder;

	public RemoteRepository(String url, ArtifactBuilder builder) {
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		this.baseUrl = url;
		this.builder = builder;
	}

	@Override
	public Artifact load(String groupId, String artifactId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<URL> getLocation(Artifact artifact) {
		Set<URL> urls = new HashSet<URL>();
		String path = artifact.toPath();
		urls.add(toURL(path));
		path = path.substring(0, path.lastIndexOf('.') - 1);
		urls.add(toURL(path + Constants.POM));
		urls.add(toURL(path + "-sources." + artifact.getType()));
		return urls;
	}

	protected URL toURL(String suffix) {
		StringBuilder stb = new StringBuilder();
		stb.append(this.baseUrl);
		stb.append('/');
		stb.append(suffix);
		return UrlUtil.toURL(stb.toString());
	}
}