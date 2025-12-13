import { useState, useEffect, useRef } from 'react'
import { useQuery } from '@tanstack/react-query'

export default function useInfinitePagination({queryKey, queryFn, size = 10, enabled = true}) {
    const [allItems, setAllItems] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);

    const bottomRef = useRef(null);

    const query = useQuery({
        queryKey: [...queryKey, page],
        queryFn: () => queryFn(page, size),
        keepPreviousData: true,
        enabled: enabled,
    });

    // Fetch data
    useEffect(() => {
        if(query.data?.content) {
            setAllItems(prev => [...prev, ...query.data.content]);
            setTotalPage(query.data.page.totalPages);
        }
    }, [query.data]);

    // Intesection Ovserver
    useEffect(() => {
        if(!bottomRef.current) return;
        if(page > totalPage) return;
        const observer = new IntersectionObserver((entries) => {
            const entry = entries[0];
            if(entry.isIntersecting && !query.isFetching) {
                setPage(prev => prev + 1);
            }
        });
        observer.observe(bottomRef.current);

        return () => observer.disconnect();
    }, [query.isFetching]);

    // reset on unmount
    useEffect(() => {
        setAllItems([])
        setPage(0)
    }, [JSON.stringify(queryKey)])

    return {
        items: allItems,
        isLoading: query.isLoading,
        isError: query.isError,
        bottomRef,
    }
}