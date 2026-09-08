import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import type { Observable } from 'rxjs';
import type {
  ComposerPlanSemaineRequest,
  GenererRepasRequest,
  PlanSemaineResponse,
  ProfilRequest,
  RepasProposesResponse,
  RepasResponse,
} from './models';

/**
 * Client HTTP vers l'API Ravito. Chemins relatifs ("/api/...") : en
 * développement, proxy.conf.json les redirige vers le backend local
 * (voir angular.json) ; en production, le frontend est cense etre servi
 * derriere le meme reverse proxy que l'API.
 */
@Injectable({ providedIn: 'root' })
export class RavitoApiService {
  private readonly http = inject(HttpClient);

  proposerRepas(profil: ProfilRequest): Observable<RepasProposesResponse> {
    return this.http.post<RepasProposesResponse>('/api/repas/propositions', profil);
  }

  composerPlan(requete: ComposerPlanSemaineRequest): Observable<PlanSemaineResponse> {
    return this.http.post<PlanSemaineResponse>('/api/plans-semaine', requete);
  }

  genererRepas(requete: GenererRepasRequest): Observable<RepasResponse> {
    return this.http.post<RepasResponse>('/api/repas/generation', requete);
  }
}
