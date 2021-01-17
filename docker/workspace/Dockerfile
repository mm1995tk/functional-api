FROM ubuntu:20.04

ENV TZ Asia/Tokyo
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update -y && \
    apt-get upgrade -y && \
    apt-get install -y git fish fzf vim curl zip unzip locales-all 
RUN curl https://git.io/fisher --create-dirs -sLo ~/.config/fish/functions/fisher.fish
RUN ["/usr/bin/fish", "-c", "fisher install jethrokuan/fzf"]
RUN git clone https://github.com/asdf-vm/asdf.git ~/.asdf --branch v0.8.0
RUN ["/usr/bin/fish", "-c", "mkdir -p ~/.config/fish/completions; and cp ~/.asdf/completions/asdf.fish ~/.config/fish/completions"]
RUN ["/usr/bin/fish", "-c", "source ~/.asdf/asdf.fish && asdf plugin-add java && asdf install java graalvm-20.3.0+java11 && asdf global java graalvm-20.3.0+java11"]
RUN ["/usr/bin/fish", "-c", "source ~/.asdf/asdf.fish && asdf plugin-add sbt && asdf install sbt 1.4.5 && asdf global sbt 1.4.5"]
COPY config.fish /root/.config/fish/
COPY ./.vimrc /root/
WORKDIR /root/workspace
EXPOSE 9000

