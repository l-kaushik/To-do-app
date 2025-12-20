import { useState, useEffect, useRef } from 'react'
import { useInfiniteQuery, useQuery } from '@tanstack/react-query'

export default function useInfinitePagination({queryKey, queryFn, size = 10, enabled = true}) {
    const bottomRef = useRef(null);

    const query = useInfiniteQuery({
        queryKey: [...queryKey],
        enabled: enabled,
        queryFn: ({pageParam = 0}) => queryFn(pageParam, size),
        getNextPageParam: (lastPage, allPages) => {
            const currentPage = lastPage.page.number;
            const totalPages = lastPage.page.totalPages;
            return currentPage + 1 < totalPages ? currentPage + 1 : undefined;
        },
    });

    // Fetch data
    const items = query.data?.pages.flatMap(page => page.content) ?? [];

    // Intesection Ovserver
    useEffect(() => {
        if(!bottomRef.current) return;
        if(!query.hasNextPage) return;
        const observer = new IntersectionObserver((entries) => {
            if(entries[0].isIntersecting && !query.isFetchingNextPage) {
                query.fetchNextPage();
            }
        });
        observer.observe(bottomRef.current);

        return () => observer.disconnect();
    }, [query.hasNextPage, query.isFetchingNextPage]);

    return {
        data: query.data,
        items: items,
        isLoading: query.isLoading,
        isError: query.isError,
        bottomRef,
    };
}