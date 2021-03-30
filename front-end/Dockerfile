FROM docker-image.cai-inc.com/run/nginx
COPY dist/ /usr/share/nginx/html
RUN chown -R nginx:nginx /usr/share/nginx/html
EXPOSE 80
