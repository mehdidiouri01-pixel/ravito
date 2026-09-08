import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { LIBELLES_JOUR, LIBELLES_RAYON, LIBELLES_UNITE } from './reference-data';
import type { PlanSemaineResponse, RayonMagasin } from './models';

/**
 * Genere le PDF cote client (jsPDF), sans passer par le backend : le
 * document n'est qu'une mise en forme de donnees deja recuperees par
 * l'appli, pas une nouvelle source de verite — pas besoin d'un endpoint
 * dedie pour ca.
 */
@Injectable({ providedIn: 'root' })
export class PdfExportService {
  exporterPlan(plan: PlanSemaineResponse, nombreDePersonnes: number): void {
    const doc = new jsPDF();
    let y = 18;

    doc.setFontSize(18);
    doc.text('Ravito — Plan de la semaine', 14, y);
    y += 7;

    doc.setFontSize(10);
    doc.text(`Quantités et coût calculés pour ${nombreDePersonnes} personne(s)`, 14, y);
    y += 8;

    doc.setFontSize(11);
    plan.jours.forEach((jour) => {
      doc.setFont('helvetica', 'bold');
      doc.text(LIBELLES_JOUR[jour.jour], 14, y);
      doc.setFont('helvetica', 'normal');
      y += 6;
      doc.text(`Petit-déjeuner : ${jour.petitDejeuner.nom}`, 20, y);
      y += 6;
      doc.text(`Déjeuner : ${jour.dejeuner.nom}`, 20, y);
      y += 8;
    });

    y += 2;
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.text('Liste de courses', 14, y);

    const rayons = Object.keys(plan.listeCourses.parRayon) as RayonMagasin[];
    const lignes: string[][] = [];
    rayons.forEach((rayon) => {
      plan.listeCourses.parRayon[rayon]?.forEach((ligne) => {
        lignes.push([LIBELLES_RAYON[rayon], ligne.ingredient, `${ligne.quantite} ${LIBELLES_UNITE[ligne.unite]}`]);
      });
    });

    autoTable(doc, {
      startY: y + 4,
      head: [['Rayon', 'Ingrédient', 'Quantité']],
      body: lignes,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [47, 107, 79] },
    });

    // jspdf-autotable enrichit l'instance jsPDF a l'execution (pas de type
    // officiel pour ce champ) : on lit sa position finale pour ne pas
    // ecrire le total par-dessus le tableau.
    const finY = (doc as unknown as { lastAutoTable?: { finalY: number } }).lastAutoTable?.finalY ?? y + 10;

    doc.setFontSize(12);
    doc.setFont('helvetica', 'bold');
    doc.text(`Coût estimé pour la semaine : ${plan.prixEstime.montant.toFixed(2)} €`, 14, finY + 10);

    doc.save('ravito-plan-semaine.pdf');
  }
}
