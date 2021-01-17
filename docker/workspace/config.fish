# .fishrc

################## Title ########################
# This is template
#################################################

################## fzfの設定 ####################
set -U FZF_LEGACY_KEYBINDINGS 0
set -U FZF_REVERSE_ISEARCH_OPTS "--reverse --height=100%"
#################################################

################# 自動でls ######################
functions --copy cd standard_cd
function cd
    standard_cd $argv; and ls
end
#################################################

################## asdfの設定 ########################
source ~/.asdf/asdf.fish
source ~/.asdf/plugins/java/set-java-home.fish
#################################################


# ローカル開発環境のDB設定
set -x JDBC_DATABASE_URL "jdbc:postgresql://postgres:5432/postgres?user=postgres&password=postgres"
