package org.monet.space.mobile.guice;

import org.monet.space.mobile.db.DatabaseRepository;
import org.monet.space.mobile.db.Repository;

import roboguice.inject.SharedPreferencesName;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bindConstant().annotatedWith(SharedPreferencesName.class).to("org.monet.space.mobile_preferences");
    bind(Repository.class).to(DatabaseRepository.class);
  }

}
