# MOS : Serveur Node.js + page html web
# annex : Android

3 solutions possibles pour faire le streaming :
- Changer l'encodage de données avant l'envoie (voir class CameraPreview, fonction processFrame, projet android)
- Trouver comment décoder les arrayBuffer (voir index.html, projet web)
- N'utiliser pas ce code et trouver une autre solution (un autre protocol autre que TCP)
