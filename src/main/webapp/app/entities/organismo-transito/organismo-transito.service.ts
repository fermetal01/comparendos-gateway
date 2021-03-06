import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IOrganismoTransito } from 'app/shared/model/organismo-transito.model';

type EntityResponseType = HttpResponse<IOrganismoTransito>;
type EntityArrayResponseType = HttpResponse<IOrganismoTransito[]>;

@Injectable({ providedIn: 'root' })
export class OrganismoTransitoService {
  public resourceUrl = SERVER_API_URL + 'api/organismo-transitos';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/organismo-transitos';

  constructor(protected http: HttpClient) {}

  create(organismoTransito: IOrganismoTransito): Observable<EntityResponseType> {
    return this.http.post<IOrganismoTransito>(this.resourceUrl, organismoTransito, { observe: 'response' });
  }

  update(organismoTransito: IOrganismoTransito): Observable<EntityResponseType> {
    return this.http.put<IOrganismoTransito>(this.resourceUrl, organismoTransito, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IOrganismoTransito>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganismoTransito[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganismoTransito[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
