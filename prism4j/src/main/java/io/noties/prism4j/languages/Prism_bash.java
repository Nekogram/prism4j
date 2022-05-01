package io.noties.prism4j.languages;

import io.noties.prism4j.*;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.languages.GrammarUtils.*;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Aliases("shell")
public class Prism_bash {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final String envVars = "\\b(?:BASH|BASHOPTS|BASH_ALIASES|BASH_ARGC|BASH_ARGV|BASH_CMDS|BASH_COMPLETION_COMPAT_DIR|BASH_LINENO|BASH_REMATCH|BASH_SOURCE|BASH_VERSINFO|BASH_VERSION|COLORTERM|COLUMNS|COMP_WORDBREAKS|DBUS_SESSION_BUS_ADDRESS|DEFAULTS_PATH|DESKTOP_SESSION|DIRSTACK|DISPLAY|EUID|GDMSESSION|GDM_LANG|GNOME_KEYRING_CONTROL|GNOME_KEYRING_PID|GPG_AGENT_INFO|GROUPS|HISTCONTROL|HISTFILE|HISTFILESIZE|HISTSIZE|HOME|HOSTNAME|HOSTTYPE|IFS|INSTANCE|JOB|LANG|LANGUAGE|LC_ADDRESS|LC_ALL|LC_IDENTIFICATION|LC_MEASUREMENT|LC_MONETARY|LC_NAME|LC_NUMERIC|LC_PAPER|LC_TELEPHONE|LC_TIME|LESSCLOSE|LESSOPEN|LINES|LOGNAME|LS_COLORS|MACHTYPE|MAILCHECK|MANDATORY_PATH|NO_AT_BRIDGE|OLDPWD|OPTERR|OPTIND|ORBIT_SOCKETDIR|OSTYPE|PAPERSIZE|PATH|PIPESTATUS|PPID|PS1|PS2|PS3|PS4|PWD|RANDOM|REPLY|SECONDS|SELINUX_INIT|SESSION|SESSIONTYPE|SESSION_MANAGER|SHELL|SHELLOPTS|SHLVL|SSH_AUTH_SOCK|TERM|UID|UPSTART_EVENTS|UPSTART_INSTANCE|UPSTART_JOB|UPSTART_SESSION|USER|WINDOWID|XAUTHORITY|XDG_CONFIG_DIRS|XDG_CURRENT_DESKTOP|XDG_DATA_DIRS|XDG_GREETER_DATA_DIR|XDG_MENU_PREFIX|XDG_RUNTIME_DIR|XDG_SEAT|XDG_SEAT_PATH|XDG_SESSION_DESKTOP|XDG_SESSION_ID|XDG_SESSION_PATH|XDG_SESSION_TYPE|XDG_VTNR|XMODIFIERS)\\b";

        final Pattern entity = pattern(compile("\\\\(?:[abceEfnrtv\\\\\"]|O?[0-7]{1,3}|U[0-9a-fA-F]{8}|u[0-9a-fA-F]{4}|x[0-9a-fA-F]{1,2})"));

        final Pattern[] variable = {
                pattern(compile("\\$?\\(\\([\\s\\S]+?\\)\\)"), false, true, null, grammar("inside",
                        token("variable", pattern(compile("(^\\$\\(\\([\\s\\S]+)\\)\\)"), true), pattern(compile("^\\$\\(\\("))),
                        token("number", pattern(compile("\\b0x[\\dA-Fa-f]+\\b|(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[Ee]-?\\d+)?"))),
                        token("operator", pattern(compile("--|\\+=|\\+\\+|\\*\\*=?|<<=?|>>=?|&&|\\|\\||[=!+\\-*/%<>^&|]=?|[?~:]"))),
                        token("punctuation", pattern(compile("\\(\\(?|\\)\\)?|,|;")))
                )),
                pattern(compile("\\$\\((?:\\([^)]+\\)|[^()])+\\)|`[^`]+`"), false, true, null, grammar("inside",
                        token("variable", pattern(compile("^\\$\\(|^`|\\)$|`$"))),
                        token("comment", pattern(compile("(^|[^\"{\\\\$])#.*"), true)),
                        token("function-name",
                                pattern(compile("(\\bfunction\\s+)[\\w-]+(?=(?:\\s*\\(?:\\s*\\))?\\s*\\{)"), true, false, "function"),
                                pattern(compile("\\b[\\w-]+(?=\\s*\\(\\s*\\)\\s*\\{)"), false, false, "function")
                        ),
                        token("for-or-select", pattern(compile("(\\b(?:for|select)\\s+)\\w+(?=\\s+in\\s)"), true, false, "variable")),
                        token("assign-left", pattern(compile("(^|[\\s;|&]|[<>]\\()\\w+(?=\\+?=)"), true, false, "variable",
                                grammar("inside", token("environment", pattern(compile("(^|[\\s;|&]|[<>]\\()" + envVars), true, false, "constant")))
                        )),
                        token("string",
                                pattern(compile("((?:^|[^<])<<-?\\s*)(\\w+)\\s[\\s\\S]*?(?:\\r?\\n|\\r)\\2"), true, true, null, grammar("inside",
                                        token("bash", pattern(compile("(^([\"']?)\\w+\\2)[ \\t]+\\S.*"), true, false, "punctuation")),
                                        //token("variable", variable),
                                        token("entity", entity)
                                )),
                                pattern(compile("((?:^|[^<])<<-?\\s*)([\"'])(\\w+)\\2\\s[\\s\\S]*?(?:\\r?\\n|\\r)\\3"), true, true, null, grammar("inside",
                                        token("bash", pattern(compile("(^([\"']?)\\w+\\2)[ \\t]+\\S.*"), true, false, "punctuation"))
                                )),
                                pattern(compile("(^|[^\\\\](?:\\\\\\\\)*)\"(?:\\\\[\\s\\S]|\\$\\([^)]+\\)|\\$(?!\\()|`[^`]+`|[^\"\\\\`$])*\""), true, true, null, grammar("inside",
                                        token("bash", pattern(compile("(^([\"']?)\\w+\\2)[ \\t]+\\S.*"), true, false, "punctuation")),
                                        //token("variable", variable),
                                        token("entity", entity)
                                )),
                                pattern(compile("(^|[^$\\\\])(?:'[^']*'|\"[^\"]*\")"), true, true),
                                pattern(compile("\\$'(?:[^'\\\\]|\\\\[\\s\\S])*'"), false, true, null, grammar("inside", token("entity", entity)))
                        ),
                        token("environment", pattern(compile("\\$" + envVars), false, false, "constant")),
                        token("function", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:add|apropos|apt|apt-cache|apt-get|aptitude|aspell|automysqlbackup|awk|basename|bash|bc|bconsole|bg|bzip2|cal|cat|cfdisk|chgrp|chkconfig|chmod|chown|chroot|cksum|clear|cmp|column|comm|composer|cp|cron|crontab|csplit|curl|cut|date|dc|dd|ddrescue|debootstrap|df|diff|diff3|dig|dir|dircolors|dirname|dirs|dmesg|docker|docker-compose|du|egrep|eject|env|ethtool|expand|expect|expr|fdformat|fdisk|fg|fgrep|file|find|fmt|fold|format|free|fsck|ftp|fuser|gawk|git|gparted|grep|groupadd|groupdel|groupmod|groups|grub-mkconfig|gzip|halt|head|hg|history|host|hostname|htop|iconv|id|ifconfig|ifdown|ifup|import|install|ip|jobs|join|kill|killall|less|link|ln|locate|logname|logrotate|look|lpc|lpr|lprint|lprintd|lprintq|lprm|ls|lsof|lynx|make|man|mc|mdadm|mkconfig|mkdir|mke2fs|mkfifo|mkfs|mkisofs|mknod|mkswap|mmv|more|most|mount|mtools|mtr|mutt|mv|nano|nc|netstat|nice|nl|node|nohup|notify-send|npm|nslookup|op|open|parted|passwd|paste|pathchk|ping|pkill|pnpm|podman|podman-compose|popd|pr|printcap|printenv|ps|pushd|pv|quota|quotacheck|quotactl|ram|rar|rcp|reboot|remsync|rename|renice|rev|rm|rmdir|rpm|rsync|scp|screen|sdiff|sed|sendmail|seq|service|sftp|sh|shellcheck|shuf|shutdown|sleep|slocate|sort|split|ssh|stat|strace|su|sudo|sum|suspend|swapon|sync|tac|tail|tar|tee|time|timeout|top|touch|tr|traceroute|tsort|tty|umount|uname|unexpand|uniq|units|unrar|unshar|unzip|update-grub|uptime|useradd|userdel|usermod|users|uudecode|uuencode|v|vcpkg|vdir|vi|vim|virsh|vmstat|wait|watch|wc|wget|whereis|which|who|whoami|write|xargs|xdg-open|yarn|yes|zenity|zip|zsh|zypper)(?=$|[)\\s;|&])"), true)),
                        token("keyword", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:case|do|done|elif|else|esac|fi|for|function|if|in|select|then|until|while)(?=$|[)\\s;|&])"), true)),
                        token("builtin", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:\\.|:|alias|bind|break|builtin|caller|cd|command|continue|declare|echo|enable|eval|exec|exit|export|getopts|hash|help|let|local|logout|mapfile|printf|pwd|read|readarray|readonly|return|set|shift|shopt|source|test|times|trap|type|typeset|ulimit|umask|unalias|unset)(?=$|[)\\s;|&])"), true, false, "class-name")),
                        token("boolean", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:false|true)(?=$|[)\\s;|&])"), true)),
                        token("file-descriptor", pattern(compile("\\B&\\d\\b"), false, false, "important")),
                        token("operator", pattern(compile("\\d?<>|>\\||\\+=|=[=~]?|!=?|<<[<-]?|[&\\d]?>>|\\d[<>]&?|[<>][&=]?|&[>&]?|\\|[&|]?"), false, false, null, grammar("inside",
                                token("file-descriptor", pattern(compile("^\\d"), false, false, "important"))
                        ))),
                        token("punctuation", pattern(compile("\\$?\\(\\(?|\\)\\)?|\\.\\.|[{\\}\\[\\];\\\\]"))),
                        token("number", pattern(compile("(^|\\s)(?:[1-9]\\d*|0)(?:[.,]\\d+)?\\b"), true))

                )),
                pattern(compile("\\$\\{[^\\}]+\\}"), false, true, null, grammar("inside",
                        token("operator", pattern(compile(":[-=?+]?|[!/]|##?|%%?|\\^\\^?|,,?"))),
                        token("punctuation", pattern(compile("[\\[\\]]"))),
                        token("environment", pattern(compile("(\\{)" + envVars), true, false, "constant"))
                )),
                pattern(compile("\\$(?:\\w+|[#?*!@$])"))
        };

        final Grammar bash = grammar("bash",
                token("shebang", pattern(compile("^#!\\s*/.*"), false, false, "important")),
                token("comment", pattern(compile("(^|[^\"{\\\\$])#.*"), true)),
                token("function-name",
                        pattern(compile("(\\bfunction\\s+)[\\w-]+(?=(?:\\s*\\(?:\\s*\\))?\\s*\\{)"), true, false, "function"),
                        pattern(compile("\\b[\\w-]+(?=\\s*\\(\\s*\\)\\s*\\{)"), false, false, "function")
                ),
                token("for-or-select", pattern(compile("(\\b(?:for|select)\\s+)\\w+(?=\\s+in\\s)"), true, false, "variable")),
                token("assign-left", pattern(compile("(^|[\\s;|&]|[<>]\\()\\w+(?=\\+?=)"), true, false, "variable",
                        grammar("inside", token("environment", pattern(compile("(^|[\\s;|&]|[<>]\\()" + envVars), true, false, "constant")))
                )),
                token("environment", pattern(compile("\\$" + envVars), false, false, "constant")),
                token("variable", variable),
                token("function", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:add|apropos|apt|apt-cache|apt-get|aptitude|aspell|automysqlbackup|awk|basename|bash|bc|bconsole|bg|bzip2|cal|cat|cfdisk|chgrp|chkconfig|chmod|chown|chroot|cksum|clear|cmp|column|comm|composer|cp|cron|crontab|csplit|curl|cut|date|dc|dd|ddrescue|debootstrap|df|diff|diff3|dig|dir|dircolors|dirname|dirs|dmesg|docker|docker-compose|du|egrep|eject|env|ethtool|expand|expect|expr|fdformat|fdisk|fg|fgrep|file|find|fmt|fold|format|free|fsck|ftp|fuser|gawk|git|gparted|grep|groupadd|groupdel|groupmod|groups|grub-mkconfig|gzip|halt|head|hg|history|host|hostname|htop|iconv|id|ifconfig|ifdown|ifup|import|install|ip|jobs|join|kill|killall|less|link|ln|locate|logname|logrotate|look|lpc|lpr|lprint|lprintd|lprintq|lprm|ls|lsof|lynx|make|man|mc|mdadm|mkconfig|mkdir|mke2fs|mkfifo|mkfs|mkisofs|mknod|mkswap|mmv|more|most|mount|mtools|mtr|mutt|mv|nano|nc|netstat|nice|nl|node|nohup|notify-send|npm|nslookup|op|open|parted|passwd|paste|pathchk|ping|pkill|pnpm|podman|podman-compose|popd|pr|printcap|printenv|ps|pushd|pv|quota|quotacheck|quotactl|ram|rar|rcp|reboot|remsync|rename|renice|rev|rm|rmdir|rpm|rsync|scp|screen|sdiff|sed|sendmail|seq|service|sftp|sh|shellcheck|shuf|shutdown|sleep|slocate|sort|split|ssh|stat|strace|su|sudo|sum|suspend|swapon|sync|tac|tail|tar|tee|time|timeout|top|touch|tr|traceroute|tsort|tty|umount|uname|unexpand|uniq|units|unrar|unshar|unzip|update-grub|uptime|useradd|userdel|usermod|users|uudecode|uuencode|v|vcpkg|vdir|vi|vim|virsh|vmstat|wait|watch|wc|wget|whereis|which|who|whoami|write|xargs|xdg-open|yarn|yes|zenity|zip|zsh|zypper)(?=$|[)\\s;|&])"), true)),
                token("keyword", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:case|do|done|elif|else|esac|fi|for|function|if|in|select|then|until|while)(?=$|[)\\s;|&])"), true)),
                token("builtin", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:\\.|:|alias|bind|break|builtin|caller|cd|command|continue|declare|echo|enable|eval|exec|exit|export|getopts|hash|help|let|local|logout|mapfile|printf|pwd|read|readarray|readonly|return|set|shift|shopt|source|test|times|trap|type|typeset|ulimit|umask|unalias|unset)(?=$|[)\\s;|&])"), true, false, "class-name")),
                token("boolean", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:false|true)(?=$|[)\\s;|&])"), true)),
                token("file-descriptor", pattern(compile("\\B&\\d\\b"), false, false, "important")),
                token("operator", pattern(compile("\\d?<>|>\\||\\+=|=[=~]?|!=?|<<[<-]?|[&\\d]?>>|\\d[<>]&?|[<>][&=]?|&[>&]?|\\|[&|]?"), false, false, null, grammar("inside",
                        token("file-descriptor", pattern(compile("^\\d"), false, false, "important"))
                ))),
                token("punctuation", pattern(compile("\\$?\\(\\(?|\\)\\)?|\\.\\.|[{\\}\\[\\];\\\\]"))),
                token("number", pattern(compile("(^|\\s)(?:[1-9]\\d*|0)(?:[.,]\\d+)?\\b"), true))
        );

        final Pattern commandAfterHeredoc = pattern(compile("(^([\"']?)\\w+\\2)[ \\t]+\\S.*"), true, false, "punctuation", bash);

        final Grammar insideString = grammar("inside",
                token("bash", commandAfterHeredoc),
                token("environment", pattern(compile("\\$" + envVars))),
                token("variable", variable),
                token("entity", entity)
        );

        bash.insertBeforeToken("environment", token("string",
                pattern(compile("((?:^|[^<])<<-?\\s*)(\\w+)\\s[\\s\\S]*?(?:\\r?\\n|\\r)\\2"), true, true, null, insideString),
                pattern(compile("((?:^|[^<])<<-?\\s*)([\"'])(\\w+)\\2\\s[\\s\\S]*?(?:\\r?\\n|\\r)\\3"), true, true, null, grammar("inside",
                        token("bash", commandAfterHeredoc)
                )),
                pattern(compile("(^|[^\\\\](?:\\\\\\\\)*)\"(?:\\\\[\\s\\S]|\\$\\([^)]+\\)|\\$(?!\\()|`[^`]+`|[^\"\\\\`$])*\""), true, true, null, insideString),
                pattern(compile("(^|[^$\\\\])(?:'[^']*'|\"[^\"]*\")"), true, true),
                pattern(compile("\\$'(?:[^'\\\\]|\\\\[\\s\\S])*'"), false, true, null, grammar("inside", token("entity", entity)))
        ));

        return bash;
    }

}
