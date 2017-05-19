using UnityEditor.iOS.Xcode;
using UnityEditor.Callbacks;
using UnityEditor;
using System.IO;


	public class PlistBuildPostprocessor
	{
		[PostProcessBuild]
		public static void OnPostprocessBuild(BuildTarget buildTarget, string path)
		{
			if (buildTarget == BuildTarget.iOS || buildTarget == BuildTarget.tvOS)
			{
				string projPath = PBXProject.GetPBXProjectPath(path);

				PBXProject proj = new PBXProject();
				proj.ReadFromString(File.ReadAllText(projPath));

				string targetName = PBXProject.GetUnityTargetName();
				string target = proj.TargetGuidByName(targetName);

				//proj.AddFileToBuild(target, proj.AddFile("Plugins/Mofiler.framework", "Frameworks/Mofiler.framework", PBXSourceTree.Build));
			proj.SetBuildProperty(target, "ENABLE_BITCODE", "YES");
                proj.SetBuildProperty(target, "EMBEDDED_CONTENT_CONTAINS_SWIFT", "YES");

				File.WriteAllText(projPath, proj.WriteToString());
			}
		}

	}
