package ontey.api.config.yaml.comment;

import ontey.api.config.comment.KeyTree;
import ontey.api.config.yaml.file.YamlConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.HasSize;
import org.llorllale.cactoos.matchers.HasValues;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class YamlKeyTreeTest {
	
	@Test
	void add() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String defaultNodeKey = "default-node-key";
		final String nodeWithCommentsKey = "node-with-comment-key";
		tree.add(defaultNodeKey);
		tree.add(nodeWithCommentsKey);
		final KeyTree.Node defaultnode = tree.get(defaultNodeKey);
		final KeyTree.Node nodeWithComment = tree.get(nodeWithCommentsKey);
		final String commentOfNodeWithComment = "Comment of node-with-comment";
		final String sideCommentOfNodeWithComment = "Side comment of node-with-comment";
		nodeWithComment.setComment(commentOfNodeWithComment);
		nodeWithComment.setSideComment(sideCommentOfNodeWithComment);
		
		MatcherAssert.assertThat(
		  "The node has a root!",
		  defaultnode.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node's name is not correct!",
		  defaultnode.getName(),
		  new IsEqual<>(defaultNodeKey)
		);
		MatcherAssert.assertThat(
		  "There is a comment!",
		  defaultnode.getComment(),
		  new IsNull<>()
		);
		MatcherAssert.assertThat(
		  "There is a side comment!",
		  defaultnode.getSideComment(),
		  new IsNull<>()
		);
		
		MatcherAssert.assertThat(
		  "The node has a root!",
		  nodeWithComment.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node's name is not correct!",
		  nodeWithComment.getName(),
		  new IsEqual<>(nodeWithCommentsKey)
		);
		MatcherAssert.assertThat(
		  "There is not a a comment!",
		  nodeWithComment.getComment(),
		  new IsEqual<>(commentOfNodeWithComment)
		);
		MatcherAssert.assertThat(
		  "There is not a side comment!",
		  nodeWithComment.getSideComment(),
		  new IsEqual<>(sideCommentOfNodeWithComment)
		);
	}
	
	@Test
	void findParent() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String nodeKey = "node-key";
		tree.add(nodeKey);
		final KeyTree.Node node = tree.get(nodeKey);
		node.add("test-child");
		node.add("test-child-2");
		final KeyTree.Node parent = tree.findParent(2);
		
		MatcherAssert.assertThat(
		  "The node has a root!",
		  parent.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node's name is not correct!",
		  parent.getName(),
		  new IsEqual<>(nodeKey)
		);
		MatcherAssert.assertThat(
		  "There is a a comment!",
		  parent.getComment(),
		  new IsNull<>()
		);
		MatcherAssert.assertThat(
		  "There is a side comment!",
		  parent.getSideComment(),
		  new IsNull<>()
		);
	}
	
	@Test
	void get() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String nodeKey = "node-key";
		tree.add(nodeKey);
		final KeyTree.Node node = tree.get(nodeKey);
		
		MatcherAssert.assertThat(
		  "The node has a root!",
		  node.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node's name is not correct!",
		  node.getName(),
		  new IsEqual<>(nodeKey)
		);
		MatcherAssert.assertThat(
		  "There is a comment!",
		  node.getComment(),
		  new IsNull<>()
		);
		MatcherAssert.assertThat(
		  "There is a side comment!",
		  node.getSideComment(),
		  new IsNull<>()
		);
	}
	
	@Test
	void getChild() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		
		final KeyTree.Node child = tree.add("test").add("child");
		final KeyTree.Node child2 = child.add("child2");
		final KeyTree.Node child3 = child.add("child3");
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  child2,
		  new IsSame<>(tree.get("test.child.child2"))
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child").getFirst(),
		  new IsSame<>(tree.get("test.child").get(0))
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child").getLast(),
		  new IsSame<>(tree.get("test.child").get(child.size() - 1))
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child").getLast(),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child[0]"),
		  new IsSame<>(child2)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child[-1]"),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  child.get("[-1]"),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  child.get(-1),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child[1]"),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child.[1]"),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  child.get(1),
		  new IsSame<>(child3)
		);
		
		MatcherAssert.assertThat(
		  "The node is not correct!",
		  tree.get("test.child[2]"),
		  new IsNull<>()
		);
	}
	
	@Test
	void keys() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String nodeKey1 = "node-key-1";
		final String nodeKey2 = "node-key-2";
		final String nodeKey3 = "node-key-3";
		tree.add(nodeKey1);
		tree.add(nodeKey2);
		tree.add(nodeKey3);
		final Set<String> keys = tree.keys();
		
		MatcherAssert.assertThat(
		  "There are not 3 node in the tree!",
		  keys.size(),
		  new IsEqual<>(3)
		);
		MatcherAssert.assertThat(
		  "The node's key are not correct!",
		  keys,
		  new HasValues<>(nodeKey1, nodeKey2, nodeKey3)
		);
	}
	
	@Test
	void entries() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String nodeKey1 = "node-key-1";
		final String nodeKey2 = "node-key-2";
		final String nodeKey3 = "node-key-3";
		tree.add(nodeKey1);
		tree.add(nodeKey2);
		tree.add(nodeKey3);
		var entries = tree.entries();
		
		MatcherAssert.assertThat(
		  "There are not 3 node in the tree!",
		  entries,
		  new HasSize(3)
		);
		MatcherAssert.assertThat(
		  "The node's key are not correct!",
		  entries.stream().map(Map.Entry::getKey).collect(Collectors.toList()),
		  new HasValues<>(nodeKey1, nodeKey2, nodeKey3)
		);
	}
	
	@Test
	void testToString() {
		final YamlConfig configuration = new YamlConfig();
		final KeyTree tree = new KeyTree(configuration.options());
		final String nodeKey = "node-key";
		tree.add(nodeKey);
		final String expected = "{indent=0, path='', name='', comment=, side=, isList=false, children=(1)[node-key]}";
		
		MatcherAssert.assertThat(
		  "toString returns a wrong value.",
		  tree.toString(),
		  new IsEqual<>(expected)
		);
	}
}
