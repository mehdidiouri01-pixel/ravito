import { Component } from '@angular/core';
import { PlanificateurComponent } from './planificateur/planificateur';

@Component({
  selector: 'app-root',
  imports: [PlanificateurComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {}
