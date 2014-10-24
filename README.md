# Projet de Programmation 2 - Aquarium distribué ![Image](http://marc-alexandre.espiaut.2.emi.u-bordeaux.fr/images/keen_thumbs_up.png)

## Description générale ![Image](http://marc-alexandre.espiaut.2.emi.u-bordeaux.fr/images/dopefish_face.png)
L'objectif de ce projet est de réaliser un aquarium distribué : un ensemble d'objets qui évoluent (des poissons ou autres choses) dans un cadre fermé 2D (l'aquarium). Ces objets "vivent" sur des machines différentes, mais le rendu est commun. Cela signifie que lorsqu'on lance l'aquarium sur plusieurs machines, chaque machine fait évoluer ses propres objets, et affiche à l'écran non seulement ses objets, mais aussi les objets des autres machines, chaque machine devant donc envoyer le nouvel état de ses objets à toutes les autres machines pour qu'elles mettent à jour leur affichage. Si le programme est arrêté sur une des machines, les objets correspondant disparaissent de l'écran des autres machines. Si le programme est
lancé sur une nouvelle machine, les nouveaux objets qui évoluent dedans apparaissent.

![Image](http://marc-alexandre.espiaut.2.emi.u-bordeaux.fr/images/dopefish_dangerous.png)
