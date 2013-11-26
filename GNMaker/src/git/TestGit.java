package git;

/** GIT sample code with BSD license. Copyright by Steve Jin */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.ResolveMerger;
import org.eclipse.jgit.merge.ResolveMerger.MergeFailureReason;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
 
 
public class TestGit
{
	public TestGit() 
	{
	}
	
	boolean testPush() {
		System.out.println("***** PUSH *****");
		File gitWorkDir = new File("/home/dutech/Projets/GIT/GNMaker");
	    try {
			Git git = Git.open(gitWorkDir);
			Repository repo = git.getRepository();
		    System.out.println("REP-------------------\n"+repo.toString());
		    
		    // Liste toute les branches
			ListBranchCommand listBranch = git.branchList();
			List<Ref> res = listBranch.call();
			for (Ref ref : res) {
				System.out.println("REF : "+ref.getName());
			}
			
			// Push Command
			PushCommand push = git.push();
			System.out.println("push.Remote ="+push.getRemote());
			System.out.println("push.Repository ="+push.getRepository());
			push.add("git");
			List<RefSpec> lref = push.getRefSpecs();
			for (RefSpec refSpec : lref) {
				System.out.println("RefSpec : "+refSpec.toString());
			}
			Iterable<PushResult> pushRes = push.call();
			for (PushResult pushResult : pushRes) {
				System.out.println("res="+pushResult.getMessages());
				System.out.println("str="+pushResult.toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	boolean testPull() {
		System.out.println("***** PULL *****");
		File gitWorkDir = new File("/home/dutech/Projets/GIT/GNTest");
	    try {
			Git git = Git.open(gitWorkDir);
			Repository repo = git.getRepository();
		    System.out.println("REP-------------------\n"+repo.toString());
		    
		    PullCommand pull = git.pull();
		    System.out.println("pull.Rep : "+pull.getRepository());
		    PullResult pullRes = pull.call();
		    System.out.println("fetchFrom ="+pullRes.getFetchedFrom());
		    FetchResult fetchRes = pullRes.getFetchResult();
		    System.out.println("fetchResu ="+fetchRes.getMessages());
		    MergeResult mergeRes = pullRes.getMergeResult();
		    System.out.println("mergeStatus : "+mergeRes.getMergeStatus().name());
		    ObjectId[] mergeCommits = mergeRes.getMergedCommits();
		    for (ObjectId objectId : mergeCommits) {
				System.out.println("mergeCommit : " +objectId.getName());
			}
		    
		    Map<String,ResolveMerger.MergeFailureReason> mergeFail = mergeRes.getFailingPaths();
		    if (mergeFail == null) {
		    	System.out.println("No conflict");
		    }
		    else {
		    	for (Entry<String, MergeFailureReason> entry : mergeFail.entrySet()) {
		    		System.out.println("Fail : "+entry.getKey()+" => "+entry.getValue());
		    	}
		    }
		    Map<String,int[][]> mergeConflict = mergeRes.getConflicts();
		    for (String path : mergeConflict.keySet()) {
		    	int[][] c = mergeConflict.get(path);
		    	System.out.println("Conflicts in file " + path);
		    	for (int i = 0; i < c.length; ++i) {
		    		System.out.println("  Conflict #" + i);
		    		for (int j = 0; j < (c[i].length); ++j) {
		    			if (c[i][j] >= 0)
		    				System.out.println("    Chunk for "
		    						+ mergeRes.getMergedCommits()[j] + " starts on line #"
		    						+ c[i][j]);
		    		}
		    	}
		    }
		    //System.out.println("rebaseRes ="+pullRes.getRebaseResult());
		    
		    
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (WrongRepositoryStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DetachedHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return true;
	}  
	
	
  public static void main(String[] args) throws Exception
  {
	  TestGit app = new TestGit();
	  app.testPush(); // Push from GNMaker
	  app.testPull(); // Pull into GNTest
  }
  
  
  
  	void testOri() throws Exception {
    File gitWorkDir = new File("/home/dutech/Projets/GIT/GNTest");
    Git git = Git.open(gitWorkDir);

    Repository repo = git.getRepository();
    System.out.println("REP-------------------\n"+repo.toString());
 
    ObjectId lastCommitId = repo.resolve(Constants.HEAD);
    System.out.println("HEAD------------------\n"+lastCommitId.toString());
 
    RevWalk revWalk = new RevWalk(repo);
    RevCommit commit = revWalk.parseCommit(lastCommitId);
 
    RevTree tree = commit.getTree();
    System.out.println("TREE------------------\n"+tree.toString());
    
    // On va essayer de parcourir
    FileTreeIterator fileIt = new FileTreeIterator(repo);
 
    TreeWalk treeWalk = new TreeWalk(repo);
    treeWalk.addTree(tree);
    treeWalk.setRecursive(true);
    treeWalk.setFilter(PathFilter.create("test.txt"));
    
    // Premier élément
    System.out.println("ROOT------------------\n"+treeWalk.getNameString());
    
    if (!treeWalk.next())
    {
      System.out.println("Nothing found!");
      return;
    }
 
    ObjectId objectId = treeWalk.getObjectId(0);
    ObjectLoader loader = repo.open(objectId);
 
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    loader.copyTo(out);
    System.out.println("story_ori.xml:\n" + out.toString());
    }
}