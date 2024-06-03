package ro.mpp2024.services;

import ro.mpp2024.model.Participation;

public interface IObserver {
   void participationAdded(Participation participation) throws CompetitionException;
}
