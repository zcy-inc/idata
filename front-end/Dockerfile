FROM docker-image.cai-inc.com/base/node:latest
COPY ./ /app/
WORKDIR /app
RUN npm install --registry=http://mirrors-front.cai-inc.com
RUN npm run build

FROM docker-image.cai-inc.com/run/nginx
WORKDIR /app
COPY --from=0 /app/dist /usr/share/nginx/html
RUN chown -R nginx:nginx /usr/share/nginx/html
EXPOSE 80
