package com.accenture.dpcs.hero;

import com.accenture.dpcs.hero.listeners.HeroReportListener;
import com.accenture.dpcs.hero.listeners.HeroTestListener;
import com.accenture.dpcs.hero.logs.HeroLogger;
import com.accenture.dpcs.hero.models.Timestampable;
import com.accenture.dpcs.hero.utils.HeroReportUtils;
import com.accenture.dpcs.hero.utils.HeroUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Listeners({HeroTestListener.class, HeroReportListener.class})
public class HeroBase {
    protected ThreadLocal<HeroLogger> log = new ThreadLocal<HeroLogger>();

    @BeforeMethod(alwaysRun = true)
    protected void beforeHeroBaseMethod(Method method, Object[] parameters) {
        log.set(new HeroLogger());
        initialExecutionLog(method, parameters);
    }

    @AfterMethod(alwaysRun = true)
    protected void afterHeroBaseMethod() {
        log.get().flush();
    }

    public HeroLogger getLog() {
        return log.get();
    }

    public List<Timestampable> getAllEvents() {
        List<? extends Timestampable> logRecords = getLog().getRecords();
        List<Timestampable> events = new ArrayList<Timestampable>(logRecords.size());
        events.addAll(logRecords);
        return HeroUtils.sort(events);
    }

    protected void initialExecutionLog(Method method, Object[] parameters) {
        getLog().info("Executing " + HeroReportUtils.getTestCaseFullName(method, parameters) + "...");
    }
}
