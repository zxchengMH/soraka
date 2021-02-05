kubectl get pods -n namespace
kubectl get nodes 

ingress-nginx
kubectl get nodes -n ingress-nginx -owide

将pod的配置写再同一个yaml文件中，可方便更好的创建与删除
kubectl apply -f xxx.yaml
kubectl delete -f xxx.yaml