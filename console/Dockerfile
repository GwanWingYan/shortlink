FROM nginx
COPY default.conf /etc/nginx/conf.d/default.conf
COPY ./dist /var/www/html/
EXPOSE 80
CMD [ "nginx", "-g", "daemon off;" ]