# jeofetch

version 0.0.1

![](jeofetch.gif)
## Install

Download the binaries from the [latest release](https://github.com/Jonathan-Zollinger/jeofetch/releases) and 
add them to your path. On windows I like to add edits to my $env:Path
in [my profile](https://github.com/Jonathan-Zollinger/powershell-profile/blob/29d23b7c2b10ae0c1cf120097294bcf7e084f26e/Microsoft.PowerShell_profile.ps1#L32-L37) with something like the following: 
```PowerShell
@("wherever\you\want\jeofetch\bin") | ForEach-Object {
    if (! ($env:Path -like "*$_*")) {
        $env:Path = "$($env:Path);$_"
    }
}
```

### Roadmap

See issues tagged with `enhancement` for future plans; a more formal roadmap coming.