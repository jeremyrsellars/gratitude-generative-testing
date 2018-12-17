using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.IO;
using System.Security.Permissions;

namespace mdwatch
{
    class Program
    {
        static FileSystemWatcher watcher = new FileSystemWatcher();

        public static void Main(string[] args)
        {
            if (args.Length < 1)
            {
                Console.WriteLine("Usage: mdwatch.exe <directory>");
                return;
            }

            Run(args[0]);
        }

        [PermissionSet(SecurityAction.Demand, Name = "FullTrust")]
        public static void Run(string path)
        {
            // Create a new FileSystemWatcher and set its properties.
            watcher.Path = path;
            /* Watch for changes in LastAccess and LastWrite times, and
               the renaming of files or directories. */
            watcher.NotifyFilter = NotifyFilters.LastAccess | NotifyFilters.LastWrite
               | NotifyFilters.FileName | NotifyFilters.DirectoryName;
            // Only watch text files.
            watcher.Filter = "*.md";
            watcher.IncludeSubdirectories = true;

            // Add event handlers.
            watcher.Changed += new FileSystemEventHandler(OnChanged);
            watcher.Created += new FileSystemEventHandler(OnChanged);
            watcher.Deleted += new FileSystemEventHandler(OnChanged);
            watcher.Renamed += new RenamedEventHandler(OnRenamed);

            // Begin watching.
            watcher.EnableRaisingEvents = true;

            // Wait for the user to quit the program.
            Console.WriteLine($"Watching {watcher.Path}, then saving files that reference those paths.");
            Console.WriteLine("Press \'q\' to quit the sample.");
            while (Console.Read() != 'q')
                ;
        }

        private static void OnChanged(object source, FileSystemEventArgs e)
        {
            // Specify what is done when a file is changed, created, or deleted.
            Console.WriteLine("File: " + e.FullPath + " " + e.ChangeType);
            UpdateFiles(e.FullPath);
        }

        private static void OnRenamed(object source, RenamedEventArgs e)
        {
            // Specify what is done when a file is renamed.
            Console.WriteLine("File: {0} renamed to {1}", e.OldFullPath, e.FullPath);
            UpdateFiles(e.OldFullPath, e.FullPath);
        }
        private static void UpdateFiles(params string[] paths)
        {
            foreach (var f in UpdatableFiles(paths))
            {
                Console.WriteLine($"Updating {f}");
                File.AppendAllText(f, " ");
            }
        }

        private static IEnumerable<string> UpdatableFiles(params string[] paths)
        {
            var regex =
                new Regex("(?i)" +
                string.Join("|",
                paths
                .Distinct()
                .Select(Path.GetFileName)
                .Select(Regex.Escape)
                ));
            bool ShouldUpdate(string f)
            {
                try
                {
                    return File.ReadLines(f).Any(regex.IsMatch);
                }
                catch (Exception)
                {
                    return false;
                }
            }
            return
                Directory.GetFiles(watcher.Path, "*.clj*", SearchOption.AllDirectories)
                    .Where(ShouldUpdate);
        }
    }
}
