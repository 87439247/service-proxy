package com.predic8.membrane.core.interceptor.balancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.predic8.membrane.core.Router;
import com.predic8.membrane.core.interceptor.Interceptor;
import com.predic8.membrane.core.rules.Rule;

public class BalancerUtil {

	public static List<Cluster> collectClusters(Router router) {
		ArrayList<Cluster> result = new ArrayList<>();
		for (Rule r : router.getRuleManager().getRules())
			for (Interceptor i : r.getInterceptors())
				if (i instanceof LoadBalancingInterceptor)
					result.addAll(((LoadBalancingInterceptor)i).getClusterManager().getClusters());
		return result;
	}

	public static List<String> collectBalancers(Router router) {
		ArrayList<String> result = new ArrayList<String>();
		for (Rule r : router.getRuleManager().getRules())
			for (Interceptor i : r.getInterceptors())
				if (i instanceof LoadBalancingInterceptor)
					result.add(((LoadBalancingInterceptor)i).getName());
		return result;
	}

	public static Balancer lookupBalancer(Router router, String name) {
		for (Rule r : router.getRuleManager().getRules())
			for (Interceptor i : r.getInterceptors())
				if (i instanceof LoadBalancingInterceptor)
					if (((LoadBalancingInterceptor)i).getName().equals(name))
						return ((LoadBalancingInterceptor) i).getClusterManager();
		throw new RuntimeException("balancer with name \"" + name + "\" not found.");
	}
	
	public static boolean hasLoadBalancing(Router router) {
		for (Rule r : router.getRuleManager().getRules())
			for (Interceptor i : r.getInterceptors())
				if (i instanceof LoadBalancingInterceptor)
					return true;
		return false;
	}

	public static void up(Router router, String balancerName, String cName, String host, int port) {
		lookupBalancer(router, balancerName).up(cName, host, port);
	}

	public static void down(Router router, String balancerName, String cName, String host, int port) {
		lookupBalancer(router, balancerName).down(cName, host, port);
	}

	public static void takeout(Router router, String balancerName, String cName, String host, int port) {
		lookupBalancer(router, balancerName).takeout(cName, host, port);
	}

	public static List<Node> getAllNodesByCluster(Router router, String balancerName, String cName) {
		return lookupBalancer(router, balancerName).getAllNodesByCluster(cName);
	}

	public static List<Node> getAvailableNodesByCluster(Router router, String balancerName, String cName) {
		return lookupBalancer(router, balancerName).getAvailableNodesByCluster(cName);
	}

	public static void addSession2Cluster(Router router, String balancerName, String sessionId, String cName, Node n) {
		lookupBalancer(router, balancerName).addSession2Cluster(sessionId, cName, n);
	}
	
	public static void removeNode(Router router, String balancerName, String cluster, String host, int port) {
		lookupBalancer(router, balancerName).removeNode(cluster, host, port);
	}

	public static Node getNode(Router router, String balancerName, String cluster, String host, int port) {
		return lookupBalancer(router, balancerName).getNode(cluster, host, port);
	}

	public static Map<String, Session> getSessions(Router router, String balancerName, String cluster) {
		return lookupBalancer(router, balancerName).getSessions(cluster);
	}

	public static List<Session> getSessionsByNode(Router router, String balancerName, String cName, Node node) {
		return lookupBalancer(router, balancerName).getSessionsByNode(cName, node);
	}


}
