import { useEffect, useState } from 'react';
import { fetchScheduleBySlug } from '../schedule.utils';
import ScheduleView from './ScheduleView';
import { ScheduleData } from '../schedule.types';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchRoute } from '../../route/route.utils';
import { TransitRoute } from '../../route/route.types';
import api from '../../api';

const ScheduleViewContainer: React.FC = () => {
    const { routeSlug } = useParams<{ routeSlug: string }>();
    
    const [loadingSchedule, setLoadingSchedule] = useState(true);
    const [loadingRoute, setLoadingRoute] = useState(true);
    const [route, setRoute] = useState<TransitRoute>();
    const [schedule, setSchedule] = useState<ScheduleData>();
    const [nextDeparture, setNextDeparture] = useState<Record<string, string[]> | undefined>();

    // Schedule data
    useEffect(() => {
        const fetchScheduleData = async () => {
            try {
                const resp = await fetchScheduleBySlug(routeSlug || '');
                setLoadingSchedule(false);
                setSchedule({...resp.data.scheduleData, collectedOn: resp.data.collectedOn});
            } catch (error) {
                console.error('Failed to fetch schedule:', error);
            }
        };

        fetchScheduleData();
    }, [routeSlug]);

    // Route data for labels
    useEffect(() => {
        const fetchRouteLabels = async () => {
            try {
                const resp = await fetchRoute(routeSlug || '');
                if (resp) {
                    setRoute(resp as TransitRoute);
                }
                setLoadingRoute(false);
            } catch (error) {
                console.error('Failed to fetch route for schedule labels:', error);
                setLoadingRoute(false);
            }
        };

        fetchRouteLabels();
    }, [routeSlug]);

    const labelsMap = new Map<string, string>();
    
    if (schedule && route) {
        labelsMap.set(schedule.onwardSchedule.terminal, route.to);
        labelsMap.set(schedule.returnSchedule.terminal, route.from);
    }

    // Next departure data
    useEffect(() => {
        const fetchNextDeparture = async () => {
            try {
                const resp = await api.get(routeSlug ? `/schedule/${routeSlug}/next` : '');
                if (resp) {
                    setNextDeparture(resp.data as Record<string, string[]>);
                }
            } catch (error) {
                console.error('Error retrieving next departure:', error);
            }
        };

        fetchNextDeparture();
        const interval = setInterval(fetchNextDeparture, 60000); // Refresh every minute
        return () => clearInterval(interval);
    }, [routeSlug]);

    const formattedNextDep = {} as Record<string, string | undefined>;
    if (route && nextDeparture) {
        Object.keys(nextDeparture).map(key => formattedNextDep[key] = (nextDeparture[key].length > 0 ? nextDeparture[key][0] : undefined));
    }

    const navigate = useNavigate();

    return <ScheduleView schedule={schedule} loading={loadingSchedule || loadingRoute} substituteLabels={labelsMap}
                routeCode={routeSlug} routeTitle={route?.displayName} nextDepartures={formattedNextDep} navBack={() => navigate(`/${routeSlug}/insights`)} />;
};

export default ScheduleViewContainer;